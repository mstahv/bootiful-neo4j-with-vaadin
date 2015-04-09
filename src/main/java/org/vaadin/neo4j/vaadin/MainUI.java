package org.vaadin.neo4j.vaadin;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author mattitahvonenitmill
 */
@SpringUI(path = "")
@Title("Another UI")
@Theme("valo")
@Widgetset("org.vaadin.neo4j.vaadin.AppWidgetSet")
@JavaScript("http://cdn.alloyui.com/2.5.0/aui/aui-min.js")
@StyleSheet("http://cdn.alloyui.com/2.5.0/aui-css/css/bootstrap.min.css")
class MainUI extends UI {
    
    @Autowired
    PersonView personView;
    @Autowired
    ProjectView projectView;
    @Autowired
    VisualEditor visualEditor;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        TabSheet tabSheet = new TabSheet(personView, projectView, visualEditor);
        tabSheet.setSizeFull();
        setContent(
                new MVerticalLayout(
                        new RichText().withMarkDownResource("/welcome.md"),
                        tabSheet
                ).withFullHeight().expand(tabSheet)
        );
    }
}
