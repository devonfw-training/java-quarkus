package org.example.app.general.dataaccess;

import org.example.app.general.common.ApplicationEntity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * Abstract base class for all persistent entities of this app.
 */
@MappedSuperclass
public abstract class ApplicationPersistenceEntity implements ApplicationEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Version
  private Integer version;

  @Override
  public Long getId() {

    return this.id;
  }

  @Override
  public void setId(Long id) {

    this.id = id;
  }

  @Override
  public Integer getVersion() {

    return this.version;
  }

  @Override
  public void setVersion(Integer version) {

    this.version = version;
  }

}
