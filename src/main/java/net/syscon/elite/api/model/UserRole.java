package net.syscon.elite.api.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * User Role
 **/
@SuppressWarnings("unused")
@ApiModel(description = "User Role")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserRole {
    @JsonIgnore
    private Map<String, Object> additionalProperties;

    @NotNull
    private Long roleId;

    @NotBlank
    private String roleCode;

    @NotBlank
    private String roleName;

    private String parentRoleCode;

    private String caseloadId;

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties == null ? new HashMap<>() : additionalProperties;
    }

    @ApiModelProperty(hidden = true)
    @JsonAnySetter
    public void setAdditionalProperties(final Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /**
     * Role Id
     */
    @ApiModelProperty(required = true, value = "Role Id")
    @JsonProperty("roleId")
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }

    /**
     * code for this role
     */
    @ApiModelProperty(required = true, value = "code for this role")
    @JsonProperty("roleCode")
    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(final String roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * Full text description of the role type
     */
    @ApiModelProperty(required = true, value = "Full text description of the role type")
    @JsonProperty("roleName")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    /**
     * role code of the parent role
     */
    @ApiModelProperty(value = "role code of the parent role")
    @JsonProperty("parentRoleCode")
    public String getParentRoleCode() {
        return parentRoleCode;
    }

    public void setParentRoleCode(final String parentRoleCode) {
        this.parentRoleCode = parentRoleCode;
    }

    /**
     * caseload that this role belongs to, (NOMIS only)
     */
    @ApiModelProperty(value = "caseload that this role belongs to, (NOMIS only)")
    @JsonProperty("caseloadId")
    public String getCaseloadId() {
        return caseloadId;
    }

    public void setCaseloadId(final String caseloadId) {
        this.caseloadId = caseloadId;
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();

        sb.append("class UserRole {\n");

        sb.append("  roleId: ").append(roleId).append("\n");
        sb.append("  roleCode: ").append(roleCode).append("\n");
        sb.append("  roleName: ").append(roleName).append("\n");
        sb.append("  parentRoleCode: ").append(parentRoleCode).append("\n");
        sb.append("  caseloadId: ").append(caseloadId).append("\n");
        sb.append("}\n");

        return sb.toString();
    }
}
