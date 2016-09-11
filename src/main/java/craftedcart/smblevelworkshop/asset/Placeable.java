package craftedcart.smblevelworkshop.asset;

import craftedcart.smblevelworkshop.util.LogHelper;
import craftedcart.smblevelworkshop.util.PosXYZ;
import org.jetbrains.annotations.NotNull;

/**
 * @author CraftedCart
 *         Created on 10/09/2016 (DD/MM/YYYY)
 */
public class Placeable implements Cloneable {

    @NotNull private IAsset asset;

    @NotNull private PosXYZ position = new PosXYZ();
    @NotNull private PosXYZ rotation = new PosXYZ();
    @NotNull private PosXYZ scale = new PosXYZ(1, 1, 1);

    public Placeable(@NotNull IAsset asset) {
        this.asset = asset;
    }

    public void setAsset(@NotNull IAsset asset) {
        this.asset = asset;
    }

    @NotNull
    public IAsset getAsset() {
        return asset;
    }

    public void setPosition(@NotNull PosXYZ position) {
        this.position = position;
    }

    @NotNull
    public PosXYZ getPosition() {
        return position;
    }

    public void setRotation(@NotNull PosXYZ rotation) {
        this.rotation = rotation;
    }

    @NotNull
    public PosXYZ getRotation() {
        return rotation;
    }

    public void setScale(@NotNull PosXYZ scale) {
        this.scale = scale;
    }

    @NotNull
    public PosXYZ getScale() {
        return scale;
    }

    public Placeable getCopy() {
        try {
            return (Placeable) clone();
        } catch (CloneNotSupportedException e) {
            LogHelper.error(getClass(), "Failed to clone Placeable");
            LogHelper.error(getClass(), e);
            return null;
        }
    }

}
