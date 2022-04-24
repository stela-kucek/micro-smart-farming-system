/**
 * 
 */
package com.ase0401.msfsdemo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ase0401.msfsdemo.controller.form.DeviceManagementForm;
import com.ase0401.msfsdemo.controller.form.DeviceRegistrationForm;
import com.ase0401.msfsdemo.controller.form.MarketItemForm;
import com.ase0401.msfsdemo.controller.form.OrderConfirmationForm;
import com.ase0401.msfsdemo.management.MarketManagement;
import com.ase0401.msfsdemo.service.UserService;

import msfs_0401.Address;
import msfs_0401.GrowProgram;
import msfs_0401.Order;
import msfs_0401.OrderItem;
import msfs_0401.OrderStatus;
import msfs_0401.Plant;

/**
 * @author azar
 *
 */

@Controller
public class MarketController {
	@Autowired
	private UserService userService;

	@Autowired
	private MarketManagement mkmt;

	private static String INFO_MESSAGE = "";

	private static String SUCCESS_MESSAGE = "";

	private Map<String, ArrayList<Plant>> userShoppingCart = new HashMap<String, ArrayList<Plant>>();

	// key: id of the object (plant), value: quantity
	private Map<Integer, Integer> quantities = new HashMap<Integer, Integer>();

	// private int plantShoppingCartCount = 0;
	Order order = null;
	Address orderAddress = null;
	ArrayList<Plant> plantShoppingCart = new ArrayList<Plant>();

	@GetMapping("/market")
	public String showItems(Authentication auth, Model model) {

		String username = auth.getName();

		if (!userShoppingCart.containsKey(username)) {
			userShoppingCart.put(username, new ArrayList<Plant>());
			quantities = mkmt.setUpQuantities();
		}

		plantShoppingCart = userShoppingCart.get(username);

		mkmt.setUpMarketManagement(username, userService.getUserRepository());

		model.addAttribute("infoMessage", INFO_MESSAGE);
		model.addAttribute("success", SUCCESS_MESSAGE);
		model.addAttribute("quantities", quantities);

		INFO_MESSAGE = "";
		SUCCESS_MESSAGE = "";

		ArrayList<Plant> plantList = mkmt.getSellablePlants();
		ArrayList<GrowProgram> gprogramList = mkmt.getSellableGrowPrograms();

		model.addAttribute("plants", plantList);
		model.addAttribute("gprograms", gprogramList);

		model.addAttribute("plantForm", new MarketItemForm());
		model.addAttribute("gprogramForm", new MarketItemForm());

		return "market";

	}

	@PostMapping("/addPlantToCart")
	public String addToPlantCart(Authentication auth, @ModelAttribute("plantForm") MarketItemForm item, Model model) {
		String username = auth.getName();
		String chosen = item.getChosenItem();

		if (chosen == null) {
			INFO_MESSAGE = "Please select an item to add to cart";
		} else {
			int plantId = Integer.valueOf(chosen);
			Plant plant = mkmt.getPlantById(plantId);
			
			if(isPlantFromDifferentSeller(plant, username)) {
				INFO_MESSAGE = "Sorry, you cannot place an order with items from different sellers.";
				return "redirect:/market";
			}

			if (quantities.get(plantId) > 0) {
				userShoppingCart.get(username).add(plant);
				SUCCESS_MESSAGE = "A " + plant.getSpecies().getName() + " is added to your cart";
				quantities.replace(plantId, quantities.get(plantId) - 1);
			} else {
				INFO_MESSAGE = "This item is out of stock";
			}

		}

		return "redirect:/market";
	}

	@GetMapping("/shoppingCart")
	public String checkout(Authentication auth, Model model) {

		String username = auth.getName();

		mkmt.setUpMarketManagement(username, userService.getUserRepository());

		INFO_MESSAGE = "You have " + userShoppingCart.get(username).size() + " items in your cart";
		model.addAttribute("infoMessage", INFO_MESSAGE);
		model.addAttribute("success", SUCCESS_MESSAGE);

		double totalPrice = mkmt.calculateTotalPrice(userShoppingCart.get(username));

		model.addAttribute("plants", userShoppingCart.get(username));
		model.addAttribute("plantForm", new MarketItemForm());
		model.addAttribute("totalPrice", totalPrice);

		return "shoppingCart";

	}

	@PostMapping("/removeItem")
	public String removeItemFromCart(Authentication auth, @ModelAttribute("plantForm") MarketItemForm item,
			Model model) {
		String username = auth.getName();
		String chosen = item.getChosenItem();

		if (chosen == null) {
			INFO_MESSAGE = "Please select an item to remove from cart";
		} else {
			int plantId = Integer.valueOf(chosen);

			Plant plant = mkmt.getPlantById(plantId);

			userShoppingCart.get(username).remove(plant);
			quantities.replace(plantId, quantities.get(plantId) + 1);
			// plantShoppingCart = mkmt.removeItem(plant, plantShoppingCart);
			// mkmt.increasePlantQuantity(plant);
			SUCCESS_MESSAGE = plant.getSpecies().getName() + " is removed from your shopping cart";

		}

		return "redirect:/shoppingCart";
	}

	@GetMapping("/getOrderAddressForm")
	public String getOrderAddressForm(Model model) {

		model.addAttribute("confirmationForm", new OrderConfirmationForm());

		return "getOrderAddress";
	}

	@PostMapping("/orderConfirmed")
	public String orderConfirm(@ModelAttribute("confirmationForm") OrderConfirmationForm confirmationForm,
			Authentication auth, Model model) {

		String username = auth.getName();

		orderAddress = mkmt.getOrderAddress(confirmationForm.getStreet(), confirmationForm.getZipCode(),
				confirmationForm.getCity(), confirmationForm.getCountry());

		mkmt.setUpMarketManagement(username, userService.getUserRepository());

		ArrayList<Plant> plantsInCart = userShoppingCart.get(username);

		mkmt.confirmOrder(username, plantsInCart, orderAddress);

		userShoppingCart.get(username).clear();
		
		decreaseQuantityOfPlants(plantsInCart);
		
		return "orderConfirmed";

	}

	@GetMapping("/myOrders")
	public String myOrders(Authentication auth, Model model) {

		String username = auth.getName();
		mkmt.setUpMarketManagement(username, userService.getUserRepository());
		ArrayList<Order> orderList = mkmt.getOrdersFromUser(username);

		model.addAttribute("infoMessage", INFO_MESSAGE);
		model.addAttribute("success", SUCCESS_MESSAGE);

		INFO_MESSAGE = "";
		SUCCESS_MESSAGE = "";

		model.addAttribute("orders", orderList);
		model.addAttribute("orderForm", new MarketItemForm());

		return "myOrders";
 
	}
	
	@GetMapping("/getOrderItems")
	public String showOrderItems(@ModelAttribute("plantForm") MarketItemForm form, Model model) {
		String chosen = form.getChosenItem();
		
		ArrayList<OrderItem> items = mkmt.getOrderItems(Integer.valueOf(chosen));
		model.addAttribute("items", items);
		
		return "orderItems";
	}

	@GetMapping("/myOrdersToFulfill")
	public String myOrdersToFulfill(Authentication auth, Model model) {

		String username = auth.getName();
		mkmt.setUpMarketManagement(username, userService.getUserRepository());
		ArrayList<Order> orderList = mkmt.getOrdersToFulfillFromUser(username);

		model.addAttribute("infoMessage", INFO_MESSAGE);
		model.addAttribute("success", SUCCESS_MESSAGE);

		INFO_MESSAGE = "";
		SUCCESS_MESSAGE = "";

		model.addAttribute("orders", orderList);
		model.addAttribute("orderForm", new MarketItemForm());

		return "myOrdersToFulfill";

	}

	@GetMapping("/fulfillOrder")
	public String fulfillOrder(Authentication auth,@ModelAttribute("orderForm") MarketItemForm order,Model model) {
		
		
		String chosen = order.getChosenItem();
		//String username = auth.getName();
		
		if (chosen == null) {
			INFO_MESSAGE = "Please select an order to fulfill";
		} else {
			int orderId = Integer.valueOf(chosen);
			
			Order order1 = mkmt.getOrderById(orderId);
			order1.setStatus(OrderStatus.SHIPPED);

			SUCCESS_MESSAGE = " Order is shipped";
		}
		
		return "redirect:/myOrdersToFulfill";
	}
	
	
	private boolean isPlantFromDifferentSeller(Plant plant, String username) {
		if(!userShoppingCart.get(username).isEmpty()) {
			Plant plantExisting = userShoppingCart.get(username).get(0);
			if(!plantExisting.getOwner().equals(plant.getOwner())) {
				return true;
			}
		}
		return false;
	}
	
	private void decreaseQuantityOfPlants(ArrayList<Plant> plantsInCart) {
		for (Iterator<Plant> iterator = plantsInCart.iterator(); iterator.hasNext();) {
			Plant plant = (Plant) iterator.next();
			int id = plant.getId();
			quantities.replace(id, quantities.get(id) - 1);
		}
	}

}
