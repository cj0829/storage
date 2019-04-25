
package org.csr.common.storage.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.csr.core.Persistable;
import org.hibernate.annotations.GenericGenerator;


/**
 * ClassName:Association.java <br/>
 * System Name：    文件系统 <br/>
 * Date:     2016年11月3日下午3:53:33 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class Association implements Persistable<Long> {
	
	/**
	 * serialVersionUID:(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private Long foreignId = 0l;
    private Datastream datastream;
    private Integer type = 0;

    // cached values
    private transient Long cachedDSId;
    private transient Integer DSHashCode;
    private transient Long cachedFK;
    private transient Integer cachedType;

    public Association() {
    }

    public Association(Datastream datastream, Long foreignKey, Integer type) {
        this.foreignId = foreignKey;
        this.datastream = datastream;
        if (null != type)
            this.type = type;

        // cache the values
        cachedDSId = datastream.getId();
        cachedFK = foreignKey;
        if (null != type)
            cachedType = type;
        DSHashCode = datastream.hashCode();
    }

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "globalGenerator")
	@GenericGenerator(name = "globalGenerator", strategy = "org.csr.core.persistence.generator5.GlobalGenerator")
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
    public void setDatastream(Datastream datastream) {
        this.datastream = datastream;
    }

    public Long getForeignId() {
        return foreignId;
    }

    public void setForeignId(Long foreignId) {
        if (null != foreignId)
            this.foreignId = foreignId;
    }

    public void setType(Integer type) {
        if (null != type)
            this.type = type;
    }

    /**
     * Returns the foreignKey.
     * 
     * @return Object
     */
    public Long getForeignKey() {
        return foreignId;
    }

    /**
     * Returns the display item.
     * 
     * @return DisplayItem
     */
    public Datastream getDatastream() {
        return datastream;
    }

    /**
     * Returns the type.
     * 
     * @return int
     */
    public Integer getType() {
        return type;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof Association) {
            Association assoc = (Association) obj;
            if (cachedDSId == null) {
            	cachedDSId = datastream.getId();
                cachedFK = foreignId;
                cachedType = type;
                DSHashCode = datastream.hashCode();
            }
            return assoc.datastream.equals(datastream) && (assoc.foreignId == foreignId) && (assoc.type == type);
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        // XXXX do type better
        if (cachedDSId == null) {
        	cachedDSId = datastream.getId();
            cachedFK = foreignId;
            cachedType = type;
            DSHashCode = datastream.hashCode();
        }
        return (int) (cachedFK * DSHashCode + cachedType);
    }

    public String toString() {
        return "Assoication: " + datastream + "/" + foreignId + "/" + type;
    }

}
