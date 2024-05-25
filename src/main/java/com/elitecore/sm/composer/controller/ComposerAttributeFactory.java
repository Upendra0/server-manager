package com.elitecore.sm.composer.controller;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.composer.model.ASCIIComposerAttr;
import com.elitecore.sm.composer.model.ASN1ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerAttribute;
import com.elitecore.sm.composer.model.XMLComposerAttribute;

@Component
public class ComposerAttributeFactory {

	public static ComposerAttribute getParserAttributeByType(String pluginType) {
		ComposerAttribute composerAttribute = null;
		if (pluginType == null) {
			return composerAttribute;
		}
		if (EngineConstants.ASCII_COMPOSER_PLUGIN.equals(pluginType)) {
			composerAttribute = new ASCIIComposerAttr();
		} else if (EngineConstants.ASN1_COMPOSER_PLUGIN.equals(pluginType)) {
			composerAttribute = new ASN1ComposerAttribute();
		} else if (EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(pluginType)) {
			composerAttribute = new FixedLengthASCIIComposerAttribute();
		} else if (EngineConstants.XML_COMPOSER_PLUGIN.equals(pluginType)) {
			composerAttribute = new XMLComposerAttribute();
		} else {
			composerAttribute = new ComposerAttribute();
		}
		return composerAttribute;
	}
}
