package model;

import java.util.ArrayList;

import javax.swing.JList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Campaign")

/**
 * Model Class for Campaign
 * @author RahulReddy
 * @version 1.0
 * @since   2/20/2017
 */
public class Campaign {
	@XmlElement(name="Campaign_Name")
	String campaignName;
	
	//@XmlElement(name="Player Name")
	//String playerName;
	
	@XmlElement(name="Loaded_Maps")
	public ArrayList<String> maps;
	
	/**
	 * Constructor for Campaign Inintialises the name and list of maps
	 * @param campaign_Name
	 * @param addedMaps
	 */
	public Campaign(String campaign_Name,ArrayList<String> addedMaps) {
		this.campaignName=campaign_Name;
		this.maps=addedMaps;
		
	}
	/**
	 * Default Constructor for Campaign
	 */
	public Campaign(){}
	
	/**
	 * Method returns the Campaign Name
	 * @return Name of the campaign
	 */
	public String getCampaignName(){
		return campaignName;
	}
	
	
	
}