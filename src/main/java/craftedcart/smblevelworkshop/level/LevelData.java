package craftedcart.smblevelworkshop.level;

import craftedcart.smblevelworkshop.animation.AnimData;
import craftedcart.smblevelworkshop.asset.AssetFalloutY;
import craftedcart.smblevelworkshop.asset.AssetStartPos;
import craftedcart.smblevelworkshop.asset.Placeable;
import craftedcart.smblevelworkshop.resource.LangManager;
import craftedcart.smblevelworkshop.resource.model.ResourceModel;
import craftedcart.smblevelworkshop.util.WSItemGroup;
import io.github.craftedcart.fluidui.util.UIColor;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

/**
 * @author CraftedCart
 *         Created on 08/09/2016 (DD/MM/YYYY)
 */
public class LevelData {

    @NotNull private Set<ResourceModel> models = new HashSet<>();
    @NotNull private Set<File> modelObjSources = new HashSet<>();
    @NotNull private ListOrderedMap<String, WSItemGroup> itemGroupMap = new ListOrderedMap<>();
    private float leadInTime = 6.0f;
    private float maxTime = 60.0f;

    public LevelData() {
        itemGroupMap.put("STAGE_RESERVED", new WSItemGroup()); //Add item group reserved for stage elements (Start, Fallout Y)
        itemGroupMap.put("BACKGROUND_RESERVED", new WSItemGroup()); //Add item group reserved for background models
        itemGroupMap.put("Static", new WSItemGroup(UIColor.matTeal())); //Add default static item group
    }

    public void addModel(@Nullable ResourceModel model) {
        models.add(model);
    }

    @NotNull
    public Set<ResourceModel> getModels() {
        return models;
    }

    public void unloadAllModels() {
        for (ResourceModel model : models) {
            model.scene.unloadAll();
        }

        models.clear();
    }

    public void addModelObjSource(File modelObjSource) {
        modelObjSources.add(modelObjSource);
    }

    public void clearModelObjSources() {
        modelObjSources.clear();
    }

    @NotNull
    public Set<File> getModelObjSources() {
        return modelObjSources;
    }

    @NotNull
    public Map<String, Placeable> getPlacedObjects() {
        Map<String, Placeable> placedObjects = new HashMap<>();
        for (Map.Entry<String, WSItemGroup> igEntry : itemGroupMap.entrySet()) placedObjects.putAll(igEntry.getValue().getPlaceables());
        return placedObjects;
    }

    /**
     * @param name The unique name for the placeable
     * @param placeable The placeable object to add
     * @param itemGroup The name of the item group to add it to
     * @return The name of the placeable
     */
    public String addPlaceable(String name, Placeable placeable, String itemGroup) {
        return itemGroupMap.get(itemGroup).addPlaceable(name, placeable);
    }

    /**
     * @param name The unique name of the object
     * @param itemGroup The name of the item group to add it to
     */
    public void addObject(String name, String itemGroup) {
        itemGroupMap.get(itemGroup).addObject(name);
    }

    /**
     * Will be added to item group 1
     *
     * @param name The unique name for the placeable
     * @param placeable The placeable object to add
     * @return The name of the placeable
     */
    public String addPlaceable(String name, Placeable placeable) {
        return getFirstItemGroup().addPlaceable(name, placeable);
    }

    /**
     * Will be added to item group 1
     *
     * @param name The unique name of the object
     */
    public void addObject(String name) {
        getFirstItemGroup().addObject(name);
    }

    /**
     * Will be added to item group 1
     *
     * @param placeable The placeable object to add
     * @return The name of the placeable
     */
    public String addPlaceable(Placeable placeable) {
        String name = LangManager.getItem(placeable.getAsset().getName()) + " 1";

        int i = 1;
        Set<String> allNames = getAllPlaceableNames();
        while (allNames.contains(name)) {
            i++;
            name = LangManager.getItem(placeable.getAsset().getName()) + " " + String.valueOf(i);
        }

        getFirstItemGroup().addPlaceable(name, placeable);
        return name;
    }

    public Placeable removePlaceable(String name) {
        for (Map.Entry<String, WSItemGroup> entry : itemGroupMap.entrySet()) {
            if (entry.getValue().hasPlaceable(name)) {
                return entry.getValue().removePlaceable(name);
            }
        }

        //Nothing found - Return null
        return null;
    }

    public void removeObject(String name) {
        for (Map.Entry<String, WSItemGroup> entry : itemGroupMap.entrySet()) {
            if (entry.getValue().hasObject(name)) {
                entry.getValue().removeObject(name);
                return;
            }
        }
    }

    public Placeable getPlaceable(String name) {
        for (Map.Entry<String, WSItemGroup> entry : itemGroupMap.entrySet()) {
            Placeable placeable =  entry.getValue().getPlaceable(name);
            if (placeable != null) return placeable;
        }

        //Nothing found - return null
        return null;
    }

    public void replacePlaceable(String name, Placeable placeable) {
        for (Map.Entry<String, WSItemGroup> entry : itemGroupMap.entrySet()) {
            if (entry.getValue().hasPlaceable(name)) {
                entry.getValue().replacePlaceable(name, placeable);
                break;
            }
        }
    }

    public void changePlaceableItemGroup(String name, String newItemGroup) {
        Placeable placeable = removePlaceable(name);
        addPlaceable(name, placeable, newItemGroup);
    }

    public void changeObjectItemGroup(String name, String newItemGroup) {
        removeObject(name);
        addObject(name, newItemGroup);

    }

    @Deprecated
    public void clearPlacedObjects() {
        for (Map.Entry<String, WSItemGroup> entry : itemGroupMap.entrySet()) entry.getValue().clearPlaceables();
    }

    @Deprecated
    public void clearBackgroundObjects() {
//        backgroundObjects.clear();
    }

    @NotNull
    @Deprecated
    public Set<String> getBackgroundObjects() {
//        return backgroundObjects;
        return new HashSet<>();
    }

    @NotNull
    @Deprecated
    public Set<String> getBackgroundExternalObjects() {
//        return backgroundExternalObjects;
        return new HashSet<>();
    }

    public float getLeadInTime() {
        return leadInTime;
    }

    public void setLeadInTime(float leadInTime) {
        this.leadInTime = leadInTime;
    }

    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
    }

    public float getMaxTime() {
        return maxTime;
    }

    public WSItemGroup getFirstItemGroup() {
        if (itemGroupMap.containsKey("Static")) {
            //Use static item group if it exists
            return itemGroupMap.get("Static");
        } else {
            //If static doesn't exist, find the first non STAGE_RESERVED / BACKGROUND_RESERVED item group
            for (Map.Entry<String, WSItemGroup> entry : itemGroupMap.entrySet()) {
               if (!entry.getKey().equals("STAGE_RESERVED") || !entry.getKey().equals("BACKGROUND_RESERVED")) return entry.getValue();
            }

            //If no item groups exist, create one
            String igName = addItemGroup();
            return itemGroupMap.get(igName);
        }
    }

    public void addItemGroup(String name, WSItemGroup itemGroup) {
        itemGroupMap.put(name, itemGroup);
    }

    /**
     * Generates a new item group
     * @return The name of the new item group
     */
    public String addItemGroup() {
        String name = LangManager.getItem("newItemGroup") + " 1";

        int i = 1;
        Set<String> allNames = itemGroupMap.keySet();
        while (allNames.contains(name)) {
            i++;
            name = LangManager.getItem("newItemGroup") + " " + String.valueOf(i);
        }

        itemGroupMap.put(name, new WSItemGroup());

        return name;
    }

    public Set<String> getAllPlaceableNames() {
        Set<String> names = new HashSet<>();
        for (Map.Entry<String, WSItemGroup> entry : itemGroupMap.entrySet()) names.addAll(entry.getValue().getPlaceables().keySet());
        return names;
    }

    public WSItemGroup getStageReservedItemGroup() {
        return itemGroupMap.get("STAGE_RESERVED");
    }

    public WSItemGroup getBackgroundReservedItemGroup() {
        return itemGroupMap.get("BACKGROUND_RESERVED");
    }

    public Map.Entry<String, Placeable> getStartPosEntry() {
        for (Map.Entry<String, Placeable> entry : getStageReservedItemGroup().getPlaceables().entrySet()) {
            if (entry.getValue().getAsset() instanceof AssetStartPos) return entry;
        }

        //Shouldn't happen
        return null;
    }

    public Map.Entry<String, Placeable> getFalloutYEntry() {
        for (Map.Entry<String, Placeable> entry : getStageReservedItemGroup().getPlaceables().entrySet()) {
            if (entry.getValue().getAsset() instanceof AssetFalloutY) return entry;
        }

        //Shouldn't happen
        return null;
    }

    @NotNull
    public Map<String, WSItemGroup> getItemGroupMap() {
        return itemGroupMap;
    }

    @Nullable
    public WSItemGroup getPlaceableItemGroup(String name) {
        for (Map.Entry<String, WSItemGroup> entry : itemGroupMap.entrySet()) {
            if (entry.getValue().hasPlaceable(name)) {
                return entry.getValue();
            }
        }

        //Nothing found - Return null
        return null;
    }

    @Nullable
    public String getPlaceableItemGroupName(String name) {
        for (Map.Entry<String, WSItemGroup> entry : itemGroupMap.entrySet()) {
            if (entry.getValue().hasPlaceable(name)) {
                return entry.getKey();
            }
        }

        //Nothing found - Return null
        return null;
    }

    public String getObjectItemGroupName(String name) {
        for (Map.Entry<String, WSItemGroup> entry : itemGroupMap.entrySet()) {
            if (entry.getValue().hasObject(name)) {
                return entry.getKey();
            }
        }

        //Nothing found - Return null
        return null;
    }

}
