package gov.niem.tools.api.db.component;

import java.beans.JavaBean;
import java.io.Serializable;

import gov.niem.tools.api.db.base.BaseNamespaceEntity;
import lombok.Data;
import lombok.NonNull;

@Data @JavaBean
public class ComponentBasics<T extends BaseNamespaceEntity<T>> implements Serializable {

  @NonNull
  private Component<T> component;

  public String getPrefix() {
    return this.component.getPrefix();
  }

  public String getName() {
    return this.component.getName();
  }

  public String getQname() {
    return this.component.getQname();
  }

  public String getDefinition() {
    return this.component.definition;
  }

  public String getRoute() {
    return this.component.getRoute();
  }

  public Object getCategory() {
    return this.component.getCategory();
  }


}
