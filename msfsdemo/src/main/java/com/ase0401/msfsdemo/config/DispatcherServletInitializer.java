/**
 * 
 */
package com.ase0401.msfsdemo.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

	public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

		@Override
		protected Class<?>[] getRootConfigClasses() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected Class<?>[] getServletConfigClasses() {
			return new Class[] {};
		}

		@Override
		protected String[] getServletMappings() {
			return new String[] { "/" };
		}

	}
