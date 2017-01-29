package test.springmvc.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table(name="REGISTERED_APPS")
public class RegisteredApp {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(name="NAME", nullable=false)
    private String name;

    @NotEmpty
    @Column(name="URL", nullable=false)
    private String url;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="ADDED_BY", nullable=false)
    private Integer addedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisteredApp that = (RegisteredApp) o;

        if (!id.equals(that.id)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = ((id == null) ? 1 : id.hashCode());
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RegisteredApp [id=" + id + ", name=" + name+ ", url="+ url
                + ", addedBy=" + addedBy + ", description=" + description + "]";
    }
}
