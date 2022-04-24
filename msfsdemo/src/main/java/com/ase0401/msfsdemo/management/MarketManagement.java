/**
 * 
 */
package com.ase0401.msfsdemo.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ase0401.msfsdemo.constants.FarmbotCapabilities;
import com.ase0401.msfsdemo.factory.ModelFactory;
import com.ase0401.msfsdemo.repository.FarmingDeviceRepository;
import com.ase0401.msfsdemo.repository.FarmingSiteRepository;
import com.ase0401.msfsdemo.repository.GrowProgramRepository;
import com.ase0401.msfsdemo.repository.OrderRepository;
import com.ase0401.msfsdemo.repository.PlantRepository;
import com.ase0401.msfsdemo.repository.UserRepository;

import msfs_0401.Address;
import msfs_0401.FarmingDevice;
import msfs_0401.GrowProgram;
import msfs_0401.Order;
import msfs_0401.OrderItem;
import msfs_0401.OrderStatus;
import msfs_0401.Plant;
import msfs_0401.SellableItem;

/**
 * @author azar
 *
 */
@Component
public class MarketManagement {

	@Autowired
	private ModelFactory factory;

	@Autowired
	private PlantRepository plantRepo;

	@Autowired
	private GrowProgramRepository growProgramRepo;

	@Autowired
	private OrderRepository orderRepo;

	private String userName;

	private UserRepository userRepo;

	private Order order;
	// private OrderItem orderItem;

	public void setUpMarketManagement(String username, UserRepository repository) {
		this.userName = username;

	}

	public Map<Integer, Integer> setUpQuantities() {
		Map<Integer, Integer> quantities = new HashMap<Integer, Integer>();

		ArrayList<Plant> plantList = getSellablePlants();

		for (Iterator<Plant> iterator = plantList.iterator(); iterator.hasNext();) {
			Plant plant = (Plant) iterator.next();
			quantities.put(plant.getId(), plant.getQuantity());
		}

		return quantities;
	}

	public ArrayList<Plant> getSellablePlants() {

		ArrayList<Plant> plantList = plantRepo.getPlants();
		ArrayList<Plant> resultList = new ArrayList<Plant>();

		for (Iterator<Plant> iterator = plantList.iterator(); iterator.hasNext();) {
			Plant plant = (Plant) iterator.next();
			if (plant.isForSale()) {
				resultList.add(plant);

			}
		}

		return resultList;
	}

	public ArrayList<GrowProgram> getSellableGrowPrograms() {

		ArrayList<GrowProgram> plantList = growProgramRepo.getGrowPrograms();
		ArrayList<GrowProgram> resultList = new ArrayList<GrowProgram>();

		for (Iterator<GrowProgram> iterator = plantList.iterator(); iterator.hasNext();) {
			GrowProgram growProgram = (GrowProgram) iterator.next();
			if (growProgram.isForSale()) {
				// if (growProgram.getCreator() .equals(userName)) {
				resultList.add(growProgram);

			}
		}

		return resultList;
	}

	public Plant getPlantById(int plantId) {

		ArrayList<Plant> plantList = getSellablePlants();

		for (Iterator<Plant> iterator = plantList.iterator(); iterator.hasNext();) {
			Plant plant = (Plant) iterator.next();
			if (plant.getId() == plantId)
				return plant;
		}

		return null;
	}


	public double calculateTotalPrice(ArrayList<Plant> plantShoppingCart) {

		double totalPrice = 0;
		for (Iterator<Plant> iterator = plantShoppingCart.iterator(); iterator.hasNext();) {
			Plant plant = (Plant) iterator.next();
			totalPrice += plant.getPrice();
		}
		return totalPrice;
	}

	public ArrayList<Plant> addPlantToShoppingCart(Plant plant, ArrayList<Plant> plantShoppingCart) {
		int quantity = 0;
		Plant pp = plant;
		for (Iterator<Plant> iterator = plantShoppingCart.iterator(); iterator.hasNext();) {
			Plant p = (Plant) iterator.next();

			if (p.getSpecies().getName() == plant.getSpecies().getName()) {
				quantity = p.getQuantity();
				p.setQuantity(quantity++);
			}
		}

		pp.setQuantity(0);
		plantShoppingCart.add(pp);
		return plantShoppingCart;
	}

	public ArrayList<Plant> removeItem(Plant plant, ArrayList<Plant> plantShoppingCart) {

		for (Iterator<Plant> iterator = plantShoppingCart.iterator(); iterator.hasNext();) {
			Plant p = (Plant) iterator.next();
			if (p.getId() == plant.getId()) {
				plantShoppingCart.remove(p);

			}
		}

		return plantShoppingCart;
	}

	public Address getOrderAddress(String street, String zipCode, String city, String country) {

		Address orderAddress = factory.createaddress(street, zipCode, city, country);
		return orderAddress;
	}

	public void confirmOrder(String username, ArrayList<Plant> plantsInCart, Address orderAddress) {
		double totalPrice = 0;
		ArrayList<Plant> plantList = plantRepo.getPlants();

		order = factory.createNewOrder();

		for (Iterator<Plant> iterator = plantsInCart.iterator(); iterator.hasNext();) {
			Plant plant = (Plant) iterator.next();
			{

				order.getOrderItems().add(plantToOrderItem(plant));
				totalPrice += plant.getPrice();

				for (Iterator<Plant> iterator1 = plantList.iterator(); iterator1.hasNext();) {
					Plant plant1 = (Plant) iterator1.next();
					{

						if (plant1.getId() == plant.getId()) {

							plantRepo.getPlantById(plant1.getId()).setQuantity((plant1.getQuantity() - 1));
						}

					}
				} // end of for loop to update plant inventory

			}
		}

		int newId = orderRepo.getNextId();

		while (idExists(newId)) {
			newId += 1;
		}

		order.setId(newId);
		order.setBuyer(username);
		order.setPriceTotal(totalPrice);
		order.setAddress(orderAddress);
		order.setStatus(OrderStatus.PLACED);

		orderRepo.addOrder(order);

	}

	public Order getOrderById(int id) {
		return orderRepo.getOrderById(id);
	}

	public ArrayList<OrderItem> getOrderItems(int id) {
		ArrayList<OrderItem> resultList = new ArrayList<OrderItem>();
		Order order = orderRepo.getOrderById(id);

		for (Iterator<OrderItem> iterator = order.getOrderItems().iterator(); iterator.hasNext();) {
			OrderItem orderItem = (OrderItem) iterator.next();
			resultList.add(orderItem);
		}
		return resultList;

	}

	public boolean idExists(int id) {
		ArrayList<Order> orders = orderRepo.getOrders();
		for (Iterator<Order> iterator = orders.iterator(); iterator.hasNext();) {
			Order order = (Order) iterator.next();
			if (order.getId() == id)
				return true;
		}
		return false;
	}

	public OrderItem plantToOrderItem(Plant plant) {

		OrderItem orderItem = factory.createOrderItem(plant.getId(), plant.getSpecies().getName(), plant.getPrice(), 1,
				plant.getOwner());

		return orderItem;
	}

	public ArrayList<Order> getOrdersFromUser(String username) {

		ArrayList<Order> orderList = orderRepo.getOrders();
		ArrayList<Order> resultList = new ArrayList<Order>();

		for (Iterator<Order> iterator = orderList.iterator(); iterator.hasNext();) {
			Order order = (Order) iterator.next();
			if (order.getBuyer().equals(username)) {

				resultList.add(order);

			}
		}
		return resultList;
	}

	public ArrayList<Order> getOrdersToFulfillFromUser(String username){
		
		
		ArrayList<Order> orderList = orderRepo.getOrders();
		ArrayList<Order> resultList = new ArrayList<Order>();		

		for (Iterator<Order> iterator = orderList.iterator(); iterator.hasNext();) {
			Order order = (Order) iterator.next();				
			if (order.getOrderItems().get(0).getSeller().equals(username)) {
				resultList.add(order);
			}
		
		}
		return resultList;
	}

}// end class MarketManagement
