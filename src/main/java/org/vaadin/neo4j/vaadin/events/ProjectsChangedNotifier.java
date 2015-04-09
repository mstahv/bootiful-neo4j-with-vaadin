
package org.vaadin.neo4j.vaadin.events;

import com.vaadin.spring.annotation.UIScope;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

/**
 * Temporary hack until vaadin4spring event bus is converted to official Vaadin
 * Spring integration library. Currently only views per user are notified.
 * 
 * @author Matti Tahvonen
 */
@Component
@UIScope
public class ProjectsChangedNotifier {
    
    public interface Listener {
        public void onModelChanged();
    }
    
    private ArrayList<Listener> listeners = new ArrayList<>();
    
    public void subscribe(Listener l) {
        listeners.add(l);
    }
    
    public void onEvent() {
        for (Listener listener : listeners) {
            listener.onModelChanged();
        }
    }

}
