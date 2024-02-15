package fr.paulbrancieq.confeats.client.gui;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.PlaceholderCategory;
import dev.isxander.yacl3.api.utils.OptionUtils;
import dev.isxander.yacl3.gui.tab.ScrollableNavigationBar;
import fr.paulbrancieq.accessoptions.OptionsAccessHandler;
import fr.paulbrancieq.confeats.client.gui.api.YetAnotherConfigLib;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;

public class YACLScreen extends dev.isxander.yacl3.gui.YACLScreen {
  private final OptionsAccessHandler optionsAccessHandler;
  private boolean pendingChanges;
  public YACLScreen(YetAnotherConfigLib config, Screen parent, OptionsAccessHandler optionsAccessHandler) {
    super(dev.isxander.yacl3.api.YetAnotherConfigLib.createBuilder()
        .title(config.title())
        .categories(config.categories())
        .build(), parent);
    this.optionsAccessHandler = optionsAccessHandler;
    OptionUtils.forEachOptions(dev.isxander.yacl3.api.YetAnotherConfigLib.createBuilder()
        .title(config.title())
        .categories(config.categories())
        .build(), option -> option.addListener((opt, val) -> onOptionChanged(opt)));
  }
  @Override
  protected void init() {
    int currentTab = 0;

    tabNavigationBar = new ScrollableNavigationBar(this.width, tabManager, config.categories()
        .stream()
        .map(category -> {
          if (category instanceof PlaceholderCategory placeholder)
            return new PlaceholderTab(placeholder);
          return new CategoryTab(category);
        }).toList());
    tabNavigationBar.selectTab(currentTab, false);
    tabNavigationBar.init();
    ScreenRect navBarArea = tabNavigationBar.getNavigationFocus();
    tabArea = new ScreenRect(0, navBarArea.height() - 1, this.width, this.height - navBarArea.height() + 1);
    tabManager.setTabArea(tabArea);
    addDrawableChild(tabNavigationBar);

    config.initConsumer().accept(this);
  }
  @Override
  protected void finishOrSave() {
    saveButtonMessage = null;

    if (pendingChanges()) {
      optionsAccessHandler.applyOptions();
      config.saveFunction().run();
      pendingChanges = false;
      if (tabManager.getCurrentTab() instanceof CategoryTab categoryTab) {
        categoryTab.updateButtons();
      }
    }
    close();
  }
  private boolean pendingChanges() {
    return pendingChanges;
  }
  @SuppressWarnings("unused")
  private void onOptionChanged(Option<?> option) {
    pendingChanges = false;

    OptionUtils.consumeOptions(config, opt -> {
      pendingChanges |= opt.changed();
      return pendingChanges;
    });

    if (tabManager.getCurrentTab() instanceof CategoryTab categoryTab) {
      categoryTab.updateButtons();
    }
  }
}
