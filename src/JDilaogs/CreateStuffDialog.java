package JDilaogs;

import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

import GameComponents.ExtensionMethods;
import java.awt.Font;

/**
 * This class is a JDialog which helps user to create new campaign, map or character.
 * 
 * @author saiteja prasdam
 * @version 1.0
 * @since 1/29/2017
 */

@SuppressWarnings("serial")
public class CreateStuffDialog extends JDialog{
 
  /**
   * This class is a JDialog which helps user to create new campaign, map or character.
   */
  public CreateStuffDialog() {
    DialogHelper.setDialogProperties(this, "Create stuff", new Rectangle(100, 100, 640, 390));
    getContentPane().setLayout(null);
    
    initComponents();
  }

  /**
   * Initializes the UI components.
   */
  private void initComponents() {
    
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    tabbedPane.setBounds(10, 11, 615, 312);
    getContentPane().add(tabbedPane);
    
    {   //Campaign panel
        JPanel campaignPanel = new JPanel();
        campaignPanel.setBackground(Color.WHITE);
        tabbedPane.addTab("Campaigns", null, campaignPanel, null);
        initCampaignPanel(campaignPanel);  
    }
    
    {   //Maps panel 
        JPanel mapsPanel = new JPanel();
        mapsPanel.setBackground(Color.WHITE);
        tabbedPane.addTab("Maps", null, mapsPanel, null);
        initMapsPanel(mapsPanel);  
    }
    
    {   //Character panel
        JPanel characterPanel = new JPanel();
        characterPanel.setBackground(Color.WHITE);
        tabbedPane.addTab("Character", null, characterPanel, null);
        initCharacterPanel(characterPanel);  
    }
    
    {   //Items panel 
        JPanel itemsPanel = new JPanel();
        itemsPanel.setBackground(Color.WHITE);
        tabbedPane.addTab("Items", null, itemsPanel, null);
        initItemsPanel(itemsPanel);  
    }
    
    {   //Done button
        JButton btnDone = new JButton("Done");
        btnDone.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            dispose();
          }
        });
        btnDone.setBounds(536, 327, 89, 23);
        getContentPane().add(btnDone);
    }
  }

  /**
   * Initializes the Character panel.
   * @param characterPanel This contains reference character panel tab.
   */
  private void initCharacterPanel(JPanel characterPanel) {
    
      SpringLayout sl_characterPanel = new SpringLayout();
      characterPanel.setLayout(sl_characterPanel);
      
      JList<String> list = new JList<String>();
      list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
      sl_characterPanel.putConstraint(SpringLayout.NORTH, list, 10, SpringLayout.NORTH, characterPanel);
      sl_characterPanel.putConstraint(SpringLayout.WEST, list, 10, SpringLayout.WEST, characterPanel);
      sl_characterPanel.putConstraint(SpringLayout.EAST, list, 600, SpringLayout.WEST, characterPanel);
      characterPanel.add(list);
      
      JButton btnAdd = new JButton("Create");
      sl_characterPanel.putConstraint(SpringLayout.NORTH, btnAdd, 251, SpringLayout.NORTH, characterPanel);
      sl_characterPanel.putConstraint(SpringLayout.SOUTH, list, -6, SpringLayout.NORTH, btnAdd);
      sl_characterPanel.putConstraint(SpringLayout.WEST, btnAdd, -112, SpringLayout.EAST, characterPanel);
      sl_characterPanel.putConstraint(SpringLayout.EAST, btnAdd, -10, SpringLayout.EAST, characterPanel);
      characterPanel.add(btnAdd);
      
      JButton btnRemove = new JButton("Remove");
      sl_characterPanel.putConstraint(SpringLayout.NORTH, btnRemove, 6, SpringLayout.SOUTH, list);
      sl_characterPanel.putConstraint(SpringLayout.WEST, btnRemove, -114, SpringLayout.WEST, btnAdd);
      sl_characterPanel.putConstraint(SpringLayout.EAST, btnRemove, -12, SpringLayout.WEST, btnAdd);
      characterPanel.add(btnRemove);    
    
  }

  /**
   * Initializes the Map panel.
   * @param mapsPanel   This contains reference to Map panel tab.
   */
  private void initMapsPanel(JPanel mapsPanel) {
    
        SpringLayout sl_mapsPanel = new SpringLayout();
        mapsPanel.setLayout(sl_mapsPanel);
        
        JList list = new JList(ExtensionMethods.getMapsList());
        list.setFont(new Font("Tahoma", Font.BOLD, 12));
        list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        sl_mapsPanel.putConstraint(SpringLayout.NORTH, list, 10, SpringLayout.NORTH, mapsPanel);
        sl_mapsPanel.putConstraint(SpringLayout.WEST, list, 10, SpringLayout.WEST, mapsPanel);
        sl_mapsPanel.putConstraint(SpringLayout.EAST, list, 600, SpringLayout.WEST, mapsPanel);
        mapsPanel.add(list);
        
        JButton btnAdd = new JButton("Create");
        btnAdd.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {              
              new NewMapDialog(CreateStuffDialog.this);
          }
        });
        sl_mapsPanel.putConstraint(SpringLayout.NORTH, btnAdd, 251, SpringLayout.NORTH, mapsPanel);
        sl_mapsPanel.putConstraint(SpringLayout.SOUTH, list, -6, SpringLayout.NORTH, btnAdd);
        sl_mapsPanel.putConstraint(SpringLayout.WEST, btnAdd, -112, SpringLayout.EAST, mapsPanel);
        sl_mapsPanel.putConstraint(SpringLayout.EAST, btnAdd, -10, SpringLayout.EAST, mapsPanel);
        mapsPanel.add(btnAdd);
        
        JButton btnRemove = new JButton("Remove");
        sl_mapsPanel.putConstraint(SpringLayout.NORTH, btnRemove, 6, SpringLayout.SOUTH, list);
        sl_mapsPanel.putConstraint(SpringLayout.WEST, btnRemove, -114, SpringLayout.WEST, btnAdd);
        sl_mapsPanel.putConstraint(SpringLayout.EAST, btnRemove, -12, SpringLayout.WEST, btnAdd);
        mapsPanel.add(btnRemove);    
  }

  /**
   * Initializes the Items panel.
   * @param itemsPanel   This contains reference to Items panel tab.
   */
  private void initItemsPanel(JPanel itemsPanel) {
    
        SpringLayout sl_itemsPanel = new SpringLayout();
        itemsPanel.setLayout(sl_itemsPanel);
        
        JList list = new JList();
        list.setFont(new Font("Tahoma", Font.BOLD, 12));
        list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        sl_itemsPanel.putConstraint(SpringLayout.NORTH, list, 10, SpringLayout.NORTH, itemsPanel);
        sl_itemsPanel.putConstraint(SpringLayout.WEST, list, 10, SpringLayout.WEST, itemsPanel);
        sl_itemsPanel.putConstraint(SpringLayout.EAST, list, 600, SpringLayout.WEST, itemsPanel);
        itemsPanel.add(list);
        
        JButton btnAdd = new JButton("Create");
        sl_itemsPanel.putConstraint(SpringLayout.NORTH, btnAdd, 251, SpringLayout.NORTH, itemsPanel);
        sl_itemsPanel.putConstraint(SpringLayout.SOUTH, list, -6, SpringLayout.NORTH, btnAdd);
        sl_itemsPanel.putConstraint(SpringLayout.WEST, btnAdd, -112, SpringLayout.EAST, itemsPanel);
        sl_itemsPanel.putConstraint(SpringLayout.EAST, btnAdd, -10, SpringLayout.EAST, itemsPanel);
        itemsPanel.add(btnAdd);
        
        JButton btnRemove = new JButton("Remove");
        sl_itemsPanel.putConstraint(SpringLayout.NORTH, btnRemove, 6, SpringLayout.SOUTH, list);
        sl_itemsPanel.putConstraint(SpringLayout.WEST, btnRemove, -114, SpringLayout.WEST, btnAdd);
        sl_itemsPanel.putConstraint(SpringLayout.EAST, btnRemove, -12, SpringLayout.WEST, btnAdd);
        itemsPanel.add(btnRemove);    
  }

  
  /**
   * Initializes the Campaign panel.
   * @param campaignPanel   This contains reference to Campaign panel tab.
   */
  private void initCampaignPanel(JPanel campaignPanel) {
      
      SpringLayout sl_campaignPanel = new SpringLayout();
      campaignPanel.setLayout(sl_campaignPanel);
      
      JList<String> list = new JList<String>();
      list.setFont(new Font("Tahoma", Font.BOLD, 12));
      list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
      sl_campaignPanel.putConstraint(SpringLayout.NORTH, list, 10, SpringLayout.NORTH, campaignPanel);
      sl_campaignPanel.putConstraint(SpringLayout.WEST, list, 10, SpringLayout.WEST, campaignPanel);
      sl_campaignPanel.putConstraint(SpringLayout.EAST, list, 600, SpringLayout.WEST, campaignPanel);
      campaignPanel.add(list);
      
      JButton btnAdd = new JButton("Create");
      sl_campaignPanel.putConstraint(SpringLayout.NORTH, btnAdd, 251, SpringLayout.NORTH, campaignPanel);
      sl_campaignPanel.putConstraint(SpringLayout.SOUTH, list, -6, SpringLayout.NORTH, btnAdd);
      sl_campaignPanel.putConstraint(SpringLayout.WEST, btnAdd, -112, SpringLayout.EAST, campaignPanel);
      sl_campaignPanel.putConstraint(SpringLayout.EAST, btnAdd, -10, SpringLayout.EAST, campaignPanel);
      campaignPanel.add(btnAdd);
      
      JButton btnRemove = new JButton("Remove");
      sl_campaignPanel.putConstraint(SpringLayout.NORTH, btnRemove, 6, SpringLayout.SOUTH, list);
      sl_campaignPanel.putConstraint(SpringLayout.WEST, btnRemove, -114, SpringLayout.WEST, btnAdd);
      sl_campaignPanel.putConstraint(SpringLayout.EAST, btnRemove, -12, SpringLayout.WEST, btnAdd);
      campaignPanel.add(btnRemove);
  }
}