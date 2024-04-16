package committee.nova.mods.novalogin.core;

import com.google.common.eventbus.EventBus;
import committee.nova.mods.novalogin.Const;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

/**
 * NLContainer
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/17 上午1:20
 */
public class NLContainer extends DummyModContainer
{
    public NLContainer()
    {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = Const.MOD_ID + "core";
        meta.name = Const.MOD_NAME + "Core";
        meta.description = "A core mod for novalogin!";
        meta.version = "0.1.0";
        meta.authorList.add("cnlimiter");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }
}
