package game.model.strategyPattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import game.GameLauncher;
import game.components.Console;
import game.components.Dice;
import game.components.ExtensionMethods;
import game.components.GameMechanics;
import game.components.SharedVariables;
import game.model.Item;
import game.model.character.Character;
import game.views.jdialogs.DialogHelper;
import game.views.jpanels.GamePlayScreen;
import game.views.jpanels.LaunchScreen;

public class HumanPlayer implements MomentStrategy{
    
    int playerMomentCount = 0;
    boolean isAttackPerformed = false;
    private GamePlayScreen gamePlayScreen;    

    public HumanPlayer(GamePlayScreen gamePlayScreen){
        this.gamePlayScreen = gamePlayScreen;
    }
    
    @Override
    public void movePlayer(String message, int fromRowNumber, int fromColNumber, int toRowNumber, int toColNumber) {
        
        if(gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber] instanceof Character && ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).getHitScore() < 1){
            
            Object temppreviousMapCellObject = gamePlayScreen.previousMapCellObject;
            gamePlayScreen.previousMapCellObject = gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber];             
            gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber] = gamePlayScreen.character;
            gamePlayScreen.currentMap.mapData[fromRowNumber][fromColNumber] = temppreviousMapCellObject;            
            gamePlayScreen.repaintMap(); 
            
            if(gamePlayScreen.previousMapCellObject instanceof Character){
                                    
                if(ExtensionMethods.fetchAllItemNames(((Character) gamePlayScreen.previousMapCellObject)).size() < 1)
                    DialogHelper.showBasicDialog("No items found");
                                    
                else if(JOptionPane.showConfirmDialog(null, "Would you like to pick items from this dead monster", "You approched a dead monster", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    
                    if(gamePlayScreen.character.backpack.backpackItems.size() >= 10)
                        DialogHelper.showBasicDialog("Your backpack is full");
                    
                    else{
                        Set<String> characterItems = new HashSet<>();
                        
                        characterItems.addAll(ExtensionMethods.fetchAllItemNames(((Character) gamePlayScreen.previousMapCellObject)));
                        
                        JComboBox<String> itemsList = new JComboBox<String>();
                        for (String string : characterItems)
                            itemsList.addItem(string);
                        
                        JOptionPane.showMessageDialog(null, itemsList, "Select a item to pick from dead monster", JOptionPane.QUESTION_MESSAGE);
                        Entry<String, String> entry = ExtensionMethods.getByValue(((Character) gamePlayScreen.previousMapCellObject).backpack.backpackItems, itemsList.getSelectedItem().toString());                        
                        
                        if(entry == null)
                            entry = ExtensionMethods.getByValue(((Character) gamePlayScreen.previousMapCellObject).items, itemsList.getSelectedItem().toString());
                        
                        if(entry != null){
                            
                            if(((Character) gamePlayScreen.previousMapCellObject).backpack.backpackItems.remove(entry.getKey(), entry.getValue()) == false)
                                ((Character) gamePlayScreen.previousMapCellObject).items.remove(entry.getKey(), entry.getValue());
                              
                            if(gamePlayScreen.character.items.containsKey(entry.getKey()))                                
                                gamePlayScreen.character.backpack.backpackItems.put(entry.getKey(), entry.getValue());
                            else
                                gamePlayScreen.character.items.put(entry.getKey(), entry.getValue());
                            
                            gamePlayScreen.character.draw();
                            ((Character) gamePlayScreen.previousMapCellObject).draw();
                            DialogHelper.showBasicDialog("You have picked up a " + entry.getKey() + " (" + entry.getValue() + ") from a dead monster"); 
                        }
                                                                       
                    }
                }
                    
            }                               
        }
        
        else if(!gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber].equals(SharedVariables.WALL_STRING) && !(gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber] instanceof Character)){
          
            Object temppreviousMapCellObject = gamePlayScreen.previousMapCellObject;
            gamePlayScreen.previousMapCellObject = gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber];             
            gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber] = gamePlayScreen.character;
            gamePlayScreen.currentMap.mapData[fromRowNumber][fromColNumber] = temppreviousMapCellObject;              
            gamePlayScreen.repaintMap(); 
            Console.printInConsole(message);
            
            if(gamePlayScreen.previousMapCellObject instanceof Item)
                pickItemsFromChest();                            
            
            else if(gamePlayScreen.previousMapCellObject instanceof String && ((String) gamePlayScreen.previousMapCellObject).equals(SharedVariables.KEY_STRING)){
                gamePlayScreen.character.setKeyCollectedFlag(true);
                gamePlayScreen.previousMapCellObject = SharedVariables.DEFAULT_CELL_STRING;;
            }
            
            else if(gamePlayScreen.previousMapCellObject instanceof String && ((String) gamePlayScreen.previousMapCellObject).equals(SharedVariables.EXIT_DOOR_STRING)){
                if(checkIfTheObjectiveIsCompleted())
                    moveToNextMap();
                
                else
                    DialogHelper.showBasicDialog("You need to collect key (If map has one) or kill all the hostile enemies to clear this map");                        
            }
                
        }
        
        else if(gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber] instanceof Character){
            
            if(((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).getIsFriendlyMonster() == false && isAttackPerformed == false)
                attack(toRowNumber, toColNumber);
            else if(((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).getIsFriendlyMonster() == true)
                exchangeItemsFromFriendlyMonsters(toRowNumber, toColNumber);
        }

        playerMomentCount++;
        if(playerMomentCount >= 3){
            playerMomentCount = 0;
            gamePlayScreen.isTurnFinished = true;
        }
    }
    
    /**
     * This method lets player to exchange items from friendly monster
     * 
     * @param toRowNumber row number which user is trying to goto
     * @param toColNumber col number which user is trying to goto
     */
    private void exchangeItemsFromFriendlyMonsters(int toRowNumber, int toColNumber) {
       
        Character friendlyMonster = (Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber];
        
        if(JOptionPane.showConfirmDialog(null, "Do you want to exchange items from this friendly monster (" + friendlyMonster.getName() + ") ?","You approched a friendly monster", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            
            if(ExtensionMethods.fetchAllItemNames(gamePlayScreen.character).size() == 0)
                DialogHelper.showBasicDialog("You don't have any item to exchange.");
            
            else if(gamePlayScreen.character.backpack.backpackItems.size() + gamePlayScreen.character.getAllItems().size() == 0)
                DialogHelper.showBasicDialog("Enemy doesn't have any items to exchange");
            
            else if(gamePlayScreen.character.backpack.backpackItems.size() >= 10)
                DialogHelper.showBasicDialog("Your backpack is full");
            
            else{
                Set<String> characterItems = new HashSet<>();
                
                characterItems.addAll(ExtensionMethods.fetchAllItemNames(gamePlayScreen.character));
                
                JComboBox<String> itemsList = new JComboBox<String>();
                for (String string : characterItems)
                    itemsList.addItem(string);
                
                JOptionPane.showMessageDialog(null, itemsList, "Select a item to exchange", JOptionPane.QUESTION_MESSAGE);
                Entry<String, String> entry = ExtensionMethods.getByValue(gamePlayScreen.character.backpack.backpackItems, itemsList.getSelectedItem().toString());
                
                if(entry == null)
                    entry = ExtensionMethods.getByValue(gamePlayScreen.character.getAllItems(), itemsList.getSelectedItem().toString());                                       
                
                if(gamePlayScreen.character.backpack.backpackItems.remove(entry.getKey(), entry.getValue()) == false)
                    gamePlayScreen.character.items.remove(entry.getKey(), entry.getValue());
                
                if(((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).backpack.backpackItems.size() > 0){
                    
                    Random random = new Random();
                    List<String> keys = new ArrayList<String>(((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).backpack.backpackItems.keySet());                        
                    String randomKey = keys.get(random.nextInt(keys.size()));
                    Collection<String> values = ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).backpack.backpackItems.get(randomKey);
                    
                    String value = (String) values.toArray()[new Random().nextInt(values.size())];
                    ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).backpack.backpackItems.remove(randomKey, value);
                    
                    if(gamePlayScreen.character.items.containsKey(randomKey))
                        gamePlayScreen.character.backpack.backpackItems.put(randomKey, value);
                    else
                        gamePlayScreen.character.items.put(randomKey, value);
                    
                    gamePlayScreen.character.draw();
                    ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).backpack.backpackItems.put(entry.getKey(), entry.getValue());
                    ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).draw();
                    DialogHelper.showBasicDialog("You have received a " + randomKey + " (" + value + ") by the exchange");                                                                                              
                }
                
                else{                        
                    Random random = new Random();
                    List<String> keys = new ArrayList<String>(((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).items.keySet());
                    String randomKey = keys.get(random.nextInt(keys.size()));
                    String value = ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).items.get(randomKey);
                    
                    ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).items.remove(randomKey, value);                        
                    
                    if(gamePlayScreen.character.items.containsKey(randomKey))                            
                        gamePlayScreen.character.backpack.backpackItems.put(randomKey, value);                                                
                    else
                        gamePlayScreen.character.items.put(randomKey, value);
                    
                    gamePlayScreen.character.draw();
                    ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).backpack.backpackItems.put(entry.getKey(), entry.getValue());
                    ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).draw();
                    DialogHelper.showBasicDialog("You have received a " + randomKey + " (" + value + ") by the exchange");  
                }           
                
                
            }
        }
    }
    
    /**
     * This method changes the map if the player completes the current map
     */
    public void moveToNextMap() {
        
        gamePlayScreen.previousMapCellObject = new String(SharedVariables.DEFAULT_CELL_STRING);
        gamePlayScreen.character.setKeyCollectedFlag(false);
        
        if(gamePlayScreen.currentMapNumber + 1 == gamePlayScreen.campaign.getMapNames().size()){
            JOptionPane.showConfirmDialog(null, "Congrats, you have completed the campaign, you will now go back to main screen", "Map cleared", JOptionPane.PLAIN_MESSAGE);
            GameLauncher.mainFrameObject.replaceJPanel(new LaunchScreen());
        }
        
        else{
            JOptionPane.showConfirmDialog(null, "Congrats, you have cleared this map, you will now go to next map", "Map cleared", JOptionPane.PLAIN_MESSAGE);
            gamePlayScreen.currentMapNumber++;                
            gamePlayScreen.currentMap = gamePlayScreen.campaign.getMapList().get(gamePlayScreen.currentMapNumber);
            gamePlayScreen.currentMap.initalizeMapData(gamePlayScreen.character.getName());
            gamePlayScreen.character.setLevel(gamePlayScreen.character.getLevel() + 1);
            gamePlayScreen.setMapLevel();
            gamePlayScreen.initComponents();
        }
        
    }
    
    /**
     * This method return true or false to state whether the object is completed or not
     * @return true if objective is completed else false
     */
    private boolean checkIfTheObjectiveIsCompleted(){
        
        if(gamePlayScreen.character.isKeyCollected() == true)
            return true;
        
        ArrayList<Character> characters = GameMechanics.getAllCharacterObjects(gamePlayScreen.currentMap);
        for(Character character : characters)
            if(!character.isPlayer() && !character.getIsFriendlyMonster() && character.getHitScore() > 0)
                return false;
        
        int count = 0;
        for(Character character : characters)
            if(!character.getIsFriendlyMonster())
                count++;
        
        if(count == 0)
            return false;
        
        return true;
    }

    @Override
    public void attack(int toRowNumber, int toColNumber) {
                
        int damagePoints  = (new Dice(1, 20, 1)).getRollSum() + gamePlayScreen.character.getAttackBonus(); //gamePlayScreen.character.getStrengthModifier()
        
        if(damagePoints >= ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).getArmorClass()){
            
            if(gamePlayScreen.character.getWeaponObject().getItemType().equalsIgnoreCase("Melee"))
                damagePoints = (new Dice(1, 8, 1)).getRollSum() + gamePlayScreen.character.getStrengthModifier();
            else
                damagePoints = (new Dice(1, 8, 1)).getRollSum();
            
            isAttackPerformed = true;
            ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).hit(damagePoints);
            Console.printInConsole("   => you hitted a hostile monster(" + ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).getName() + ") with " + damagePoints + " attack points");    
        }
        
        else
            Console.printInConsole("   => you missed hitting a hostile monster(" + ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).getName() + " - " + ((Character) gamePlayScreen.currentMap.mapData[toRowNumber][toColNumber]).getArmorClass() + " armor class) with " + damagePoints + " attack points");                       
    }

    @Override
    public void pickItemsFromChest() {
        
        if(JOptionPane.showConfirmDialog(null, "This chest contains a " + ((Item) gamePlayScreen.previousMapCellObject).getItemType() + " (" + ((Item) gamePlayScreen.previousMapCellObject).getItemName() + "), would you like to pick it?", "You approched a chest", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            if(gamePlayScreen.character.backpack.backpackItems.size() >= 10)
                DialogHelper.showBasicDialog("Your backpack is full");
            else{
                Item item = (Item) gamePlayScreen.previousMapCellObject;                            
                
                if(gamePlayScreen.character.items.containsKey(item.itemType) && gamePlayScreen.character.items.get(item.itemType) != null)
                    gamePlayScreen.character.backpack.backpackItems.put(item.itemType, item.itemName);                            
                else    
                    gamePlayScreen.character.items.put(item.itemType, item.itemName);
                           
                gamePlayScreen.character.draw();
                gamePlayScreen.previousMapCellObject = new String(SharedVariables.DEFAULT_CELL_STRING);
                DialogHelper.showBasicDialog("Awesome, you have picked up a " + item.itemType + " (" + item.itemName + ") from a abandoned chest");
            }
        }
    }

    @Override
    public void playTurn() {
        isAttackPerformed = false;
        gamePlayScreen.playerMomentMechanics.setKeyListeners(gamePlayScreen);
    }

}