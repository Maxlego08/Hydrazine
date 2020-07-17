package com.github.hydrazine.module;

/**
 * 
 * @author xTACTIXzZ
 * 
 *         This interface represents a module
 *
 */
public interface Module {

	/**
	 * @return name
	 */
	public String getName();

	/**
	 * @return description
	 */
	public String getDescription();

	/**
	 * 
	 */
	public void start();

	/**
	 * 
	 * @param cause
	 */
	public void stop(String cause);

	/**
	 * 
	 */
	public void configure();

}
