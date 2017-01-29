package project;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by deft on 29.01.2017.
 */

public class ModifyBeanObject{
    private int field;

    public int getField() {
        return field;
    }

    @XmlAttribute
    public void setField(int field) {
        this.field = field;
    }
}
