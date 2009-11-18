package org.ddd.toolbox.jpa;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.joda.time.DateTime;

public class TrackedEntityListener {

    /**
     * @frameworkUseOnly
     */
    public TrackedEntityListener() {
    }
    
    /**
     * Set datestamps before database persistence.
     */
    @PrePersist
    public void setDtsInsert(TrackedEntity o) {
        DateTime dts = new DateTime();
        o.setDtsInsert(dts);
        o.setDtsUpdate(dts);
    }
    
    /**
     * Set datestamps before database persistence.
     */
    @PreUpdate
    public void setDtsUpdate(TrackedEntity o) {
        o.setDtsUpdate(new DateTime());
    }

}
