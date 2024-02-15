package fr.paulbrancieq.confeats.client.gui;

import com.google.gson.Gson;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.*;
import fr.paulbrancieq.accessoptions.OptionsAccessHandler;
import fr.paulbrancieq.accessoptions.commons.options.Option;
import fr.paulbrancieq.confeats.Confeats;
import fr.paulbrancieq.confeats.client.gui.api.YACLOption;
import fr.paulbrancieq.confeats.client.gui.api.YetAnotherConfigLib;
import fr.paulbrancieq.confeats.client.gui.gson.GsonConfigCategory;
import fr.paulbrancieq.confeats.client.gui.gson.GsonOption;
import fr.paulbrancieq.confeats.client.gui.gson.GsonOptionGroup;
import fr.paulbrancieq.confeats.client.gui.gson.GsonYACLScreen;
import fr.paulbrancieq.confeats.client.gui.impl.YACLBindingImpl;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class OptionsScreenBuilder {
  private final OptionsAccessHandler optionsAccessHandler;
  private final GsonYACLScreen gsonYACLScreen;
  public OptionsScreenBuilder(String jsonFilePath) throws FileNotFoundException {
    optionsAccessHandler = new OptionsAccessHandler();
    gsonYACLScreen = new Gson().fromJson(new FileReader(jsonFilePath), GsonYACLScreen.class);
  }
  public Screen generateScreen(Screen parent) {
    List<dev.isxander.yacl3.api.ConfigCategory> list = new java.util.ArrayList<>(gsonYACLScreen.getCategories().stream().map(this::createGUIOptionCategory).toList());
    list.removeIf(Objects::isNull);
    YetAnotherConfigLib yetAnotherConfigLib = YetAnotherConfigLib.createBuilder()
        .title(Text.of(gsonYACLScreen.getTitle()))
        .categories(list)
        .optionsAccessHandler(optionsAccessHandler)
        .build();
    return yetAnotherConfigLib.generateScreen(parent);
  }
  private dev.isxander.yacl3.api.Option<?> createGUIOption(GsonOption gsonOption) {
    Option<?, ?> option = gsonOption.getOption(optionsAccessHandler);
    if (option == null) {
      Confeats.getLogger().warn("Option " + gsonOption + " from storage " + gsonOption.getStorage() + " not found");
      return null;
    }
    Type type = option.getOriginalValue().getClass();
    switch (option.getOriginalValue().getClass().getSimpleName()) {
      case "Boolean" -> {
        @SuppressWarnings("unchecked")
        Option<?, Boolean> booleanOption = (Option<?, Boolean>) option;
        return YACLOption.createBuilder(booleanOption)
            .binding(new YACLBindingImpl<>(false, booleanOption, optionsAccessHandler))
            .controller(BooleanControllerBuilder::create)
            .build();
      }
      case "Integer" -> {
        @SuppressWarnings("unchecked")
        Option<?, Integer> integerOption = (Option<?, Integer>) option;
        return YACLOption.createBuilder(integerOption)
            .binding(new YACLBindingImpl<>(0, integerOption, optionsAccessHandler))
            .controller(IntegerFieldControllerBuilder::create)
            .build();
      }
      case "Long" -> {
        @SuppressWarnings("unchecked")
        Option<?, Long> longOption = (Option<?, Long>) option;
        return YACLOption.createBuilder(longOption)
            .binding(new YACLBindingImpl<>(0L, longOption, optionsAccessHandler))
            .controller(LongFieldControllerBuilder::create)
            .build();
      }
      case "Float" -> {
        @SuppressWarnings("unchecked")
        Option<?, Float> floatOption = (Option<?, Float>) option;
        return YACLOption.createBuilder(floatOption)
            .binding(new YACLBindingImpl<>(0.0F, floatOption, optionsAccessHandler))
            .controller(FloatFieldControllerBuilder::create)
            .build();
      }
      case "Double" -> {
        @SuppressWarnings("unchecked")
        Option<?, Double> doubleOption = (Option<?, Double>) option;
        return YACLOption.createBuilder(doubleOption)
            .binding(new YACLBindingImpl<>(0.0D, doubleOption, optionsAccessHandler))
            .controller(DoubleFieldControllerBuilder::create)
            .build();
      }
      case "String" -> {
        @SuppressWarnings("unchecked")
        Option<?, String> stringOption = (Option<?, String>) option;
        return YACLOption.createBuilder(stringOption)
            .binding(new YACLBindingImpl<>("", stringOption, optionsAccessHandler))
            .controller(StringControllerBuilder::create)
            .build();
      }
      default ->
          throw new IllegalArgumentException("Unsupported option type: " + type.getTypeName());
    }
  }

  private dev.isxander.yacl3.api.OptionGroup createGUIOptionGroup(GsonOptionGroup gsonOptionGroup) {
    List<dev.isxander.yacl3.api.Option<?>> list = new java.util.ArrayList<>(gsonOptionGroup.getOptions().stream().map(this::createGUIOption).toList());
    list.removeIf(Objects::isNull);
    return dev.isxander.yacl3.api.OptionGroup.createBuilder()
        .name(Text.of(gsonOptionGroup.getName()))
        .description(OptionDescription.of(Text.of(gsonOptionGroup.getDescription())))
        .options(list)
        .build();
  }

  private dev.isxander.yacl3.api.ConfigCategory createGUIOptionCategory(
      GsonConfigCategory gsonOptionCategory) {
    List<OptionGroup> list = new java.util.ArrayList<>(gsonOptionCategory.getGroups().stream().map(this::createGUIOptionGroup).toList());
    list.removeIf(Objects::isNull);
    return dev.isxander.yacl3.api.ConfigCategory.createBuilder()
        .name(Text.of(gsonOptionCategory.getName()))
        .tooltip(Text.of(gsonOptionCategory.getDescription()))
        .groups(list)
        .build();
  }
}
