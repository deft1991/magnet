package project;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by deft on 29.01.2017.
 */

@XmlRootElement
public class Entries {
    private List<BeanObject> entry;

    public List<BeanObject> getEntry() {
        return entry;
    }

    public void setEntry(List<BeanObject> entry) {
        this.entry = entry;
    }


}