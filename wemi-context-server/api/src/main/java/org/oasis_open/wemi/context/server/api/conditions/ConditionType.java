package org.oasis_open.wemi.context.server.api.conditions;

import org.oasis_open.wemi.context.server.api.rules.Rule;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

/**
 * Represents a node in the segment definition expression tree
 */
@XmlRootElement
public class ConditionType {
    String id;
    String name;
    String description;
    String template;
    String resourceBundle;
    String queryBuilderFilter;
    Set<Tag> tags = new TreeSet<Tag>();
    Set<String> tagIDs = new LinkedHashSet<String>();
    List<Parameter> parameters = new ArrayList<Parameter>();
    Rule autoCreateRule;

    public ConditionType() {
    }

    public ConditionType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(String resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public String getQueryBuilderFilter() {
        return queryBuilderFilter;
    }

    public void setQueryBuilderFilter(String queryBuilderFilter) {
        this.queryBuilderFilter = queryBuilderFilter;
    }

    @XmlElement(name = "tags")
    public Set<String> getTagIDs() {
        return tagIDs;
    }

    public void setTagIDs(Set<String> tagIDs) {
        this.tagIDs = tagIDs;
    }

    @XmlTransient
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @XmlElement(name = "parameters")
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Rule getAutoCreateRule() {
        return autoCreateRule;
    }

    public void setAutoCreateRule(Rule autoCreateRule) {
        this.autoCreateRule = autoCreateRule;
    }
}
