package com.srishti.template.repository;

import com.srishti.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findByIdAndOwnerId(Long templateId, Long ownerId);

    Boolean existsTemplateByOwnerIdAndTitle(Long ownerId, String title);

}
