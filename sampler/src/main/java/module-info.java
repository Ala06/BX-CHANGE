/* SPDX-License-Identifier: MIT */

module atlantafx.sampler {

    requires atlantafx.base;

    requires java.desktop;
    requires java.prefs;
    requires javafx.swing;
    requires javafx.media;
    requires javafx.web;
    requires jdk.zipfs;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;
    requires org.kordamp.ikonli.material2;
    requires org.jetbrains.annotations;
    exports atlantafx.sampler.services.serviceImpl to quartz;
    requires fr.brouillard.oss.cssfx;
    requires datafaker;
    requires java.sql;
    requires twilio;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires javax.mail.api;
    requires quartz;
    requires com.mailjet.api;
    requires org.json;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    exports atlantafx.sampler;
    exports atlantafx.sampler.fake.domain;
    exports atlantafx.sampler.event;
    exports atlantafx.sampler.layout;
    exports atlantafx.sampler.page;
    exports atlantafx.sampler.page.general;
    exports atlantafx.sampler.page.components;
    exports atlantafx.sampler.page.showcase;
    exports atlantafx.sampler.theme;
    exports atlantafx.sampler.util;

    opens atlantafx.sampler.fake.domain;

    // resources
    opens atlantafx.sampler;
    opens atlantafx.sampler.assets.highlightjs;
    opens atlantafx.sampler.assets.styles;
    opens atlantafx.sampler.images;
    opens atlantafx.sampler.images.modena;
    opens atlantafx.sampler.media;
    opens atlantafx.sampler.page.general;
    opens atlantafx.sampler.page.showcase;
}
