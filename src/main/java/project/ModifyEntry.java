package project;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by deft on 29.01.2017.
 */

@XmlRootElement
public class ModifyEntry  {
    private List<ModifyBeanObject> entry;

    public List<ModifyBeanObject> getEntry() {
        return entry;
    }

    public void setEntry(List<ModifyBeanObject> entry) {
        this.entry = entry;
    }
}
