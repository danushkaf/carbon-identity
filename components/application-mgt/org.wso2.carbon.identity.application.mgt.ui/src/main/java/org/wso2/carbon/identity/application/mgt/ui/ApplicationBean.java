/*
 *Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *WSO2 Inc. licenses this file to you under the Apache License,
 *Version 2.0 (the "License"); you may not use this file except
 *in compliance with the License.
 *You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 */
package org.wso2.carbon.identity.application.mgt.ui;

import org.wso2.carbon.identity.application.common.model.xsd.*;
import org.wso2.carbon.ui.util.CharacterEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationBean {

    public static final String AUTH_TYPE_DEFAULT = "default";
    public static final String AUTH_TYPE_LOCAL = "local";
    public static final String AUTH_TYPE_FEDERATED = "federated";
    public static final String AUTH_TYPE_FLOW = "flow";
    public static final String IDP_LOCAL_NAME = "LOCAL";

    public static final String LOCAL_IDP = "wso2carbon-local-idp";

    private ServiceProvider serviceProvider;
    private IdentityProvider[] federatedIdentityProviders;
    private List<IdentityProvider> enabledFederatedIdentityProviders;
    private LocalAuthenticatorConfig[] localAuthenticatorConfigs;
    private RequestPathAuthenticatorConfig[] requestPathAuthenticators;
    private Map<String, String> roleMap;
    private Map<String, String> claimMap;
    private Map<String, String> requestedClaims = new HashMap<String, String>();
    private String samlIssuer;
    private String oauthAppName;
    private String oauthConsumerSecret;
    private String attrConsumServiceIndex;
    private String wstrustEp;
    private String passivests;
    private String passiveSTSWReply;
    private String openid;
    private String[] claimUris;

    public ApplicationBean() {

    }

    public void reset() {
        serviceProvider = null;
        federatedIdentityProviders = null;
        localAuthenticatorConfigs = null;
        requestPathAuthenticators = null;
        roleMap = null;
        claimMap = null;
        requestedClaims = new HashMap<String, String>();
        ;
        samlIssuer = null;
        oauthAppName = null;
        wstrustEp = null;
        passivests = null;
        passiveSTSWReply = null;
        openid = null;
        oauthConsumerSecret = null;
        attrConsumServiceIndex = null;
        enabledFederatedIdentityProviders = null;
    }

    /**
     * @return
     */
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    /**
     * @param serviceProvider
     */
    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    /**
     * @return
     */
    public String getAuthenticationType() {
        return serviceProvider.getLocalAndOutBoundAuthenticationConfig().getAuthenticationType();
    }

    /**
     * @param type
     */
    public void setAuthenticationType(String type) {
        serviceProvider.getLocalAndOutBoundAuthenticationConfig().setAuthenticationType(type);
    }

    /**
     * @param type
     * @return
     */
    public String getStepZeroAuthenticatorName(String type) {
        if (AUTH_TYPE_LOCAL.equalsIgnoreCase(type)) {
            if (serviceProvider.getLocalAndOutBoundAuthenticationConfig().getAuthenticationSteps() != null
                    && serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                    .getAuthenticationSteps().length > 0
                    && serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                    .getAuthenticationSteps()[0].getLocalAuthenticatorConfigs() != null
                    && serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                    .getAuthenticationSteps()[0].getLocalAuthenticatorConfigs()[0] != null) {
                return serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                        .getAuthenticationSteps()[0].getLocalAuthenticatorConfigs()[0].getName();
            }
        }

        if (AUTH_TYPE_FEDERATED.equalsIgnoreCase(type)) {
            if (serviceProvider.getLocalAndOutBoundAuthenticationConfig().getAuthenticationSteps() != null
                    && serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                    .getAuthenticationSteps().length > 0
                    && serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                    .getAuthenticationSteps()[0].getFederatedIdentityProviders() != null
                    && serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                    .getAuthenticationSteps()[0].getFederatedIdentityProviders().length > 0
                    && serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                    .getAuthenticationSteps()[0].getFederatedIdentityProviders()[0] != null) {
                return serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                        .getAuthenticationSteps()[0].getFederatedIdentityProviders()[0]
                        .getIdentityProviderName();
            }
        }

        return null;
    }

    public void setStepZeroAuthenticatorName(String type, String name) {
        if (AUTH_TYPE_LOCAL.equalsIgnoreCase(type)) {
            LocalAuthenticatorConfig localAuthenticator = new LocalAuthenticatorConfig();
            localAuthenticator.setName(name);
            AuthenticationStep authStep = new AuthenticationStep();
            authStep.setLocalAuthenticatorConfigs(new LocalAuthenticatorConfig[]{localAuthenticator});

        }

    }

    /**
     * @return
     */
    public IdentityProvider[] getFederatedIdentityProviders() {
        return federatedIdentityProviders;
    }

    /**
     * @param federatedIdentityProviders
     */
    public void setFederatedIdentityProviders(IdentityProvider[] federatedIdentityProviders) {
        this.federatedIdentityProviders = federatedIdentityProviders;
    }

    public List<IdentityProvider> getEnabledFederatedIdentityProviders() {
        if (enabledFederatedIdentityProviders != null) {
            return enabledFederatedIdentityProviders;
        }
        if (federatedIdentityProviders != null && federatedIdentityProviders.length > 0) {
            enabledFederatedIdentityProviders = new ArrayList<IdentityProvider>();
            for (IdentityProvider idp : federatedIdentityProviders) {
                if (idp.getEnable()) {
                    FederatedAuthenticatorConfig[] fedAuthConfigs = idp.getFederatedAuthenticatorConfigs();
                    if (fedAuthConfigs != null && fedAuthConfigs.length > 0) {
                        for (FederatedAuthenticatorConfig config : fedAuthConfigs) {
                            if (config.getEnabled()) {
                                enabledFederatedIdentityProviders.add(idp);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return enabledFederatedIdentityProviders;
    }

    /**
     * @return
     */
    public LocalAuthenticatorConfig[] getLocalAuthenticatorConfigs() {
        return localAuthenticatorConfigs;
    }

    /**
     * @param localAuthenticatorConfigs
     */
    public void setLocalAuthenticatorConfigs(LocalAuthenticatorConfig[] localAuthenticatorConfigs) {
        this.localAuthenticatorConfigs = localAuthenticatorConfigs;
    }

    /**
     * @return
     */
    public RequestPathAuthenticatorConfig[] getRequestPathAuthenticators() {
        return requestPathAuthenticators;
    }

    /**
     * @param requestPathAuthenticators
     */
    public void setRequestPathAuthenticators(
            RequestPathAuthenticatorConfig[] requestPathAuthenticators) {
        this.requestPathAuthenticators = requestPathAuthenticators;
    }

    /**
     * @return
     */
    public List<String> getPermissions() {

        List<String> permList = new ArrayList<String>();

        if (serviceProvider != null && serviceProvider.getPermissionAndRoleConfig() != null) {
            PermissionsAndRoleConfig permissionAndRoleConfig = serviceProvider
                    .getPermissionAndRoleConfig();
            if (permissionAndRoleConfig != null) {
                ApplicationPermission[] permissions = permissionAndRoleConfig.getPermissions();
                if (permissions != null && permissions.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i] != null) {
                            permList.add(permissions[i].getValue());
                        }
                    }
                }
            }

        }

        return permList;
    }

    /**
     * @param permissions
     */
    public void setPermissions(String[] permissions) {
        ApplicationPermission[] applicationPermission = new ApplicationPermission[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            ApplicationPermission appPermission = new ApplicationPermission();
            appPermission.setValue(permissions[i]);
        }
        serviceProvider.getPermissionAndRoleConfig().setPermissions(applicationPermission);
    }

    /**
     * @return
     */
    public String getRoleClaimUri() {
        if (serviceProvider.getClaimConfig() != null) {
            return serviceProvider.getClaimConfig().getRoleClaimURI();
        } else {
            return null;
        }
    }

    /**
     * @param roleClaimUri
     */
    public void setRoleClaimUri(String roleClaimUri) {

        if (roleClaimUri != null) {
            if (serviceProvider.getClaimConfig() != null) {
                serviceProvider.getClaimConfig().setRoleClaimURI(roleClaimUri);
            } else {
                ClaimConfig claimConfig = new ClaimConfig();
                claimConfig.setRoleClaimURI(roleClaimUri);
                serviceProvider.setClaimConfig(claimConfig);
            }
        }
    }

    /**
     * @return
     */
    public String getUserClaimUri() {
        if (serviceProvider.getClaimConfig() != null) {
            return serviceProvider.getClaimConfig().getUserClaimURI();
        } else {
            return null;
        }
    }

    /**
     * @param roleClaimUri
     */
    public void setUserClaimUri(String userClaimUri) {

        if (userClaimUri != null) {
            if (serviceProvider.getClaimConfig() != null) {
                serviceProvider.getClaimConfig().setUserClaimURI(userClaimUri);
            } else {
                ClaimConfig claimConfig = new ClaimConfig();
                claimConfig.setUserClaimURI(userClaimUri);
                serviceProvider.setClaimConfig(claimConfig);
            }
        }
    }

    /**
     * @return
     */
    public Map<String, String> getRoleMapping() {

        if (serviceProvider.getPermissionAndRoleConfig() == null) {
            return new HashMap<String, String>();
        }

        RoleMapping[] roleMapping = serviceProvider.getPermissionAndRoleConfig().getRoleMappings();

        if (roleMap != null && roleMapping != null && (roleMapping.length == roleMap.size())) {
            return roleMap;
        }

        roleMap = new HashMap<String, String>();

        if (roleMapping != null) {
            for (int i = 0; i < roleMapping.length; i++) {
                roleMap.put(roleMapping[i].getLocalRole().getLocalRoleName(),
                        roleMapping[i].getRemoteRole());
            }
        }

        return roleMap;
    }

    /**
     * @param roleMapping
     */
    public void addRoleMapping(String spRole, String localRole) {
        roleMap.put(localRole, spRole);
    }

    /**
     * @return
     */
    public Map<String, String> getClaimMapping() {

        if (serviceProvider.getClaimConfig() == null) {
            return new HashMap<String, String>();
        }

        ClaimMapping[] claimMapping = serviceProvider.getClaimConfig().getClaimMappings();

        if (claimMap != null && claimMapping != null && (claimMapping.length == claimMap.size())) {
            return claimMap;
        }

        claimMap = new HashMap<String, String>();

        if (claimMapping != null) {
            for (int i = 0; i < claimMapping.length; i++) {
                if (claimMapping[i] != null && claimMapping[i].getRemoteClaim() != null
                        && claimMapping[i].getLocalClaim() != null) {
                    claimMap.put(claimMapping[i].getLocalClaim().getClaimUri(), claimMapping[i]
                            .getRemoteClaim().getClaimUri());
                    if (claimMapping[i].getRequested()) {
                        requestedClaims.put(claimMapping[i].getRemoteClaim().getClaimUri(), "true");
                    } else {
                        requestedClaims
                                .put(claimMapping[i].getRemoteClaim().getClaimUri(), "false");
                    }
                }
            }
        }

        return claimMap;
    }

    /**
     * Is Local Claims Selected
     *
     * @return
     */
    public boolean isLocalClaimsSelected() {
        if (serviceProvider.getClaimConfig() != null) {
            return serviceProvider.getClaimConfig().getLocalClaimDialect();
        }
        return true;
    }

    public boolean isAlwaysSendMappedLocalSubjectId() {
        if (serviceProvider.getClaimConfig() != null) {
            return serviceProvider.getClaimConfig().getAlwaysSendMappedLocalSubjectId();
        }
        return false;
    }

    public boolean isAlwaysSendBackAuthenticatedListOfIdPs() {
        if (serviceProvider.getLocalAndOutBoundAuthenticationConfig() != null) {
            return serviceProvider.getLocalAndOutBoundAuthenticationConfig().getAlwaysSendBackAuthenticatedListOfIdPs();
        }
        return false;
    }

    public String getSubjectClaimUri() {
        if (serviceProvider.getLocalAndOutBoundAuthenticationConfig() != null) {
            return serviceProvider.getLocalAndOutBoundAuthenticationConfig().getSubjectClaimUri();
        }
        return null;
    }

    public String getAttributeConsumingServiceIndex() {
        if (attrConsumServiceIndex != null) {
            return attrConsumServiceIndex;
        }

        InboundAuthenticationRequestConfig[] authRequests = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();
        if (authRequests != null) {
            for (InboundAuthenticationRequestConfig request : authRequests) {
                if ("samlsso".equalsIgnoreCase(request.getInboundAuthType())) {
                    if (request.getProperties() != null) {
                        for (Property property : request.getProperties()) {
                            if ("attrConsumServiceIndex".equalsIgnoreCase(property.getName())) {
                                attrConsumServiceIndex = property.getValue();
                                return attrConsumServiceIndex;
                            }
                        }
                    }
                    return null;
                }
            }
        }
        return null;
    }

    public void setAttributeConsumingServiceIndex(String attrConsumServiceIndex) {
        this.attrConsumServiceIndex = attrConsumServiceIndex;
    }

    public String getOauthConsumerSecret() {
        if (oauthConsumerSecret != null) {
            return oauthConsumerSecret;
        }

        InboundAuthenticationRequestConfig[] authRequests = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();
        if (authRequests != null) {
            for (InboundAuthenticationRequestConfig request : authRequests) {
                if ("oauth2".equalsIgnoreCase(request.getInboundAuthType())) {
                    if (request.getProperties() != null) {
                        for (Property property : request.getProperties()) {
                            if ("oauthConsumerSecret".equalsIgnoreCase(property.getName())) {
                                oauthConsumerSecret = property.getValue();
                                return oauthConsumerSecret;
                            }
                        }
                    }
                    return null;
                }
            }
        }
        return null;
    }

    public void setOauthConsumerSecret(String oauthConsumerSecret) {
        this.oauthConsumerSecret = oauthConsumerSecret;
    }

    /**
     * @return
     */
    public String getSAMLIssuer() {

        if (samlIssuer != null) {
            return samlIssuer;
        }

        InboundAuthenticationRequestConfig[] authRequest = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();

        if (authRequest != null) {
            for (int i = 0; i < authRequest.length; i++) {
                if ("samlsso".equalsIgnoreCase(authRequest[i].getInboundAuthType())) {
                    samlIssuer = authRequest[i].getInboundAuthKey();
                    break;
                }
            }
        }

        return samlIssuer;
    }

    /**
     * @param issuerName
     */
    public void setSAMLIssuer(String issuerName) {
        this.samlIssuer = issuerName;
    }

    public void deleteSAMLIssuer() {
        this.samlIssuer = null;
        this.attrConsumServiceIndex = null;
        InboundAuthenticationRequestConfig[] authRequest = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();

        if (authRequest != null && authRequest.length > 0) {
            List<InboundAuthenticationRequestConfig> tempAuthRequest = new ArrayList<InboundAuthenticationRequestConfig>();
            for (int i = 0; i < authRequest.length; i++) {
                if ("samlsso".equalsIgnoreCase(authRequest[i].getInboundAuthType())) {
                    continue;
                }
                tempAuthRequest.add(authRequest[i]);
            }
            if (tempAuthRequest.size() > 0) {
                serviceProvider
                        .getInboundAuthenticationConfig()
                        .setInboundAuthenticationRequestConfigs(
                                tempAuthRequest
                                        .toArray(new InboundAuthenticationRequestConfig[tempAuthRequest
                                                .size()]));
            } else {
                serviceProvider.getInboundAuthenticationConfig()
                        .setInboundAuthenticationRequestConfigs(null);
            }
        }
    }

    /**
     * @param oauthAppName
     */
    public void setOIDCAppName(String oauthAppName) {
        this.oauthAppName = oauthAppName;
    }

    public void deleteOauthApp() {
        this.oauthAppName = null;
        this.oauthConsumerSecret = null;
        InboundAuthenticationRequestConfig[] authRequest = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();

        if (authRequest != null && authRequest.length > 0) {
            List<InboundAuthenticationRequestConfig> tempAuthRequest = new ArrayList<InboundAuthenticationRequestConfig>();
            for (int i = 0; i < authRequest.length; i++) {
                if ("oauth2".equalsIgnoreCase(authRequest[i].getInboundAuthType())) {
                    continue;
                }
                tempAuthRequest.add(authRequest[i]);
            }
            if (tempAuthRequest.size() > 0) {
                serviceProvider
                        .getInboundAuthenticationConfig()
                        .setInboundAuthenticationRequestConfigs(
                                tempAuthRequest
                                        .toArray(new InboundAuthenticationRequestConfig[tempAuthRequest
                                                .size()]));
            } else {
                serviceProvider.getInboundAuthenticationConfig()
                        .setInboundAuthenticationRequestConfigs(null);
            }
        }
    }

    public void deleteWstrustEp() {
        this.wstrustEp = null;
        InboundAuthenticationRequestConfig[] authRequest = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();

        if (authRequest != null && authRequest.length > 0) {
            List<InboundAuthenticationRequestConfig> tempAuthRequest = new ArrayList<InboundAuthenticationRequestConfig>();
            for (int i = 0; i < authRequest.length; i++) {
                if ("wstrust".equalsIgnoreCase(authRequest[i].getInboundAuthType())) {
                    continue;
                }
                tempAuthRequest.add(authRequest[i]);
            }
            if (tempAuthRequest.size() > 0) {
                serviceProvider
                        .getInboundAuthenticationConfig()
                        .setInboundAuthenticationRequestConfigs(
                                tempAuthRequest
                                        .toArray(new InboundAuthenticationRequestConfig[tempAuthRequest
                                                .size()]));
            } else {
                serviceProvider.getInboundAuthenticationConfig()
                        .setInboundAuthenticationRequestConfigs(null);
            }
        }
    }

    /**
     * @return
     */
    public String getOIDCClientId() {

        if (oauthAppName != null) {
            return oauthAppName;
        }

        InboundAuthenticationRequestConfig[] authRequest = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();

        if (authRequest != null) {
            for (int i = 0; i < authRequest.length; i++) {
                if ("oauth2".equalsIgnoreCase(authRequest[i].getInboundAuthType())) {
                    oauthAppName = authRequest[i].getInboundAuthKey();
                    break;
                }
            }
        }

        return oauthAppName;
    }

    /**
     * @return
     */
    public String getOpenIDRealm() {

        if (openid != null) {
            return openid;
        }

        InboundAuthenticationRequestConfig[] authRequest = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();
        if (authRequest != null) {
            for (int i = 0; i < authRequest.length; i++) {
                if ("openid".equalsIgnoreCase(authRequest[i].getInboundAuthType())) {
                    openid = authRequest[i].getInboundAuthKey();
                    break;
                }
            }
        }
        return openid;
    }

    /**
     * @return
     */
    public String getWstrustSP() {
        if (wstrustEp != null) {
            return wstrustEp;
        }

        InboundAuthenticationRequestConfig[] authRequest = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();
        if (authRequest != null) {
            for (int i = 0; i < authRequest.length; i++) {
                if ("wstrust".equalsIgnoreCase(authRequest[i].getInboundAuthType())) {
                    wstrustEp = authRequest[i].getInboundAuthKey();
                    break;
                }
            }
        }

        return wstrustEp;
    }

    /**
     * @return
     */
    public String getPassiveSTSRealm() {

        if (passivests != null) {
            return passivests;
        }

        InboundAuthenticationRequestConfig[] authRequest = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();

        if (authRequest != null) {
            for (int i = 0; i < authRequest.length; i++) {
                if ("passivests".equalsIgnoreCase(authRequest[i].getInboundAuthType())) {
                    passivests = authRequest[i].getInboundAuthKey();
                    break;
                }
            }
        }

        return passivests;
    }

    /**
     *
     * @return
     */
    public String getPassiveSTSWReply() {

        if (passiveSTSWReply != null) {
            return passiveSTSWReply;
        }

        InboundAuthenticationRequestConfig[] authRequest = serviceProvider
                .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs();

        if (authRequest != null) {
            for (int i = 0; i < authRequest.length; i++) {
                if ("passivests".equalsIgnoreCase(authRequest[i].getInboundAuthType())) {

                    // get wreply url from properties
                    Property[] properties = authRequest[i].getProperties();
                    if (properties != null) {
                        for (int j = 0; j < properties.length; j++) {
                            if("passiveSTSWReply".equalsIgnoreCase(properties[j].getName())) {
                                passiveSTSWReply = properties[j].getValue();
                                break;
                            }
                        }
                    }

                    break;
                }
            }
        }

        return passiveSTSWReply;
    }

    /**
     * @return
     */
    public String[] getClaimUris() {
        return claimUris;
    }

    public void setClaimUris(String[] claimUris) {
        this.claimUris = claimUris;
    }

    /**
     * @param request
     */
    public void updateOutBoundAuthenticationConfig(HttpServletRequest request) {

        String[] authSteps = request.getParameterValues("auth_step");

        if (authSteps != null && authSteps.length > 0) {
            List<AuthenticationStep> authStepList = new ArrayList<AuthenticationStep>();

            for (String authstep : authSteps) {
                AuthenticationStep authStep = new AuthenticationStep();
                authStep.setStepOrder(Integer.parseInt(authstep));

                boolean isSubjectStep = request.getParameter("subject_step_" + authstep) != null
                        && request.getParameter("subject_step_" + authstep).equals("on") ? true
                        : false;
                authStep.setSubjectStep(isSubjectStep);

                boolean isAttributeStep = request.getParameter("attribute_step_" + authstep) != null
                        && request.getParameter("attribute_step_" + authstep).equals("on") ? true
                        : false;
                authStep.setAttributeStep(isAttributeStep);

                String[] localAuthenticatorNames = request.getParameterValues("step_" + authstep
                        + "_local_auth");

                if (localAuthenticatorNames != null && localAuthenticatorNames.length > 0) {
                    List<LocalAuthenticatorConfig> localAuthList = new ArrayList<LocalAuthenticatorConfig>();
                    for (String name : localAuthenticatorNames) {
                        if (name != null) {
                            LocalAuthenticatorConfig localAuth = new LocalAuthenticatorConfig();
                            localAuth.setName(name);
                            if (name != null && localAuthenticatorConfigs != null) {
                                for (LocalAuthenticatorConfig config : localAuthenticatorConfigs) {
                                    if (config.getName().equals(name)) {
                                        localAuth.setDisplayName(config.getDisplayName());
                                        break;
                                    }
                                }
                            }
                            localAuthList.add(localAuth);
                        }
                    }

                    if (localAuthList.size() > 0) {
                        authStep.setLocalAuthenticatorConfigs(localAuthList
                                .toArray(new LocalAuthenticatorConfig[localAuthList.size()]));
                    }

                }

                String[] federatedIdpNames = request.getParameterValues("step_" + authstep
                        + "_fed_auth");

                if (federatedIdpNames != null && federatedIdpNames.length > 0) {
                    List<IdentityProvider> fedIdpList = new ArrayList<IdentityProvider>();
                    for (String name : federatedIdpNames) {
                        if (name != null) {
                            IdentityProvider idp = new IdentityProvider();
                            idp.setIdentityProviderName(name);

                            FederatedAuthenticatorConfig authenticator = new FederatedAuthenticatorConfig();
                            authenticator.setName(CharacterEncoder.getSafeText(request.getParameter("step_" + authstep + "_idp_"
                                    + name + "_fed_authenticator")));
                            idp.setDefaultAuthenticatorConfig(authenticator);
                            idp.setFederatedAuthenticatorConfigs(new FederatedAuthenticatorConfig[]{authenticator});

                            fedIdpList.add(idp);
                        }
                    }

                    if (fedIdpList.size() > 0) {
                        authStep.setFederatedIdentityProviders(fedIdpList
                                .toArray(new IdentityProvider[fedIdpList.size()]));
                    }
                }

                if ((authStep.getFederatedIdentityProviders() != null && authStep
                        .getFederatedIdentityProviders().length > 0)
                        || (authStep.getLocalAuthenticatorConfigs() != null && authStep
                        .getLocalAuthenticatorConfigs().length > 0)) {
                    authStepList.add(authStep);
                }

            }

            if (serviceProvider.getLocalAndOutBoundAuthenticationConfig() == null) {
                serviceProvider
                        .setLocalAndOutBoundAuthenticationConfig(new LocalAndOutboundAuthenticationConfig());
            }

            if (authStepList != null && authStepList.size() > 0) {
                serviceProvider.getLocalAndOutBoundAuthenticationConfig().setAuthenticationSteps(
                        authStepList.toArray(new AuthenticationStep[authStepList.size()]));
            }

        }

    }

    /**
     * @param request
     */
    public void update(HttpServletRequest request) {

        // update basic info.
        serviceProvider.setApplicationName(CharacterEncoder.getSafeText(request.getParameter("spName")));
        serviceProvider.setDescription(CharacterEncoder.getSafeText(request.getParameter("sp-description")));
        String isSasApp = CharacterEncoder.getSafeText(request.getParameter("isSaasApp"));
        serviceProvider.setSaasApp((isSasApp != null && "on".equals(isSasApp)) ? true : false);

        if (serviceProvider.getLocalAndOutBoundAuthenticationConfig() == null) {
            // create fresh one.
            serviceProvider
                    .setLocalAndOutBoundAuthenticationConfig(new LocalAndOutboundAuthenticationConfig());
        }

        // authentication type : default, local, federated or advanced.
        serviceProvider.getLocalAndOutBoundAuthenticationConfig().setAuthenticationType(
                CharacterEncoder.getSafeText(request.getParameter("auth_type")));

        // update inbound provisioning data.
        String provisioningUserStore = CharacterEncoder.getSafeText(request.getParameter("scim-inbound-userstore"));
        InboundProvisioningConfig inBoundProConfig = new InboundProvisioningConfig();
        inBoundProConfig.setProvisioningUserStore(provisioningUserStore);
        serviceProvider.setInboundProvisioningConfig(inBoundProConfig);

        // update outbound provisioning data.
        String[] provisioningProviders = request.getParameterValues("provisioning_idp");

        if (provisioningProviders != null && provisioningProviders.length > 0) {

            List<IdentityProvider> provisioningIdps = new ArrayList<IdentityProvider>();

            for (String proProvider : provisioningProviders) {
                String connector = CharacterEncoder.getSafeText(request.getParameter("provisioning_con_idp_" + proProvider));
                String jitEnabled = CharacterEncoder.getSafeText(request.getParameter("provisioning_jit_" + proProvider));
                String blocking = CharacterEncoder.getSafeText(request.getParameter("blocking_prov_" + proProvider));
                if (connector != null) {
                    IdentityProvider proIdp = new IdentityProvider();
                    proIdp.setIdentityProviderName(proProvider);

                    JustInTimeProvisioningConfig jitpro = new JustInTimeProvisioningConfig();

                    if ("on".equals(jitEnabled)) {
                        jitpro.setProvisioningEnabled(true);
                    }

                    proIdp.setJustInTimeProvisioningConfig(jitpro);

                    ProvisioningConnectorConfig proCon = new ProvisioningConnectorConfig();
                    if ("on".equals(blocking)) {
                        proCon.setBlocking(true);
                    } else {
                        proCon.setBlocking(false);
                    }
                    proCon.setName(connector);
                    proIdp.setDefaultProvisioningConnectorConfig(proCon);
                    provisioningIdps.add(proIdp);
                }
            }

            if (provisioningIdps.size() > 0) {
                OutboundProvisioningConfig outboundProConfig = new OutboundProvisioningConfig();
                outboundProConfig.setProvisioningIdentityProviders(provisioningIdps
                        .toArray(new IdentityProvider[provisioningIdps.size()]));
                serviceProvider.setOutboundProvisioningConfig(outboundProConfig);
            }
        } else {
            serviceProvider.setOutboundProvisioningConfig(new OutboundProvisioningConfig());
        }

        // get all request-path authenticators.
        String[] requestPathAuthenticators = request.getParameterValues("req_path_auth");

        if (requestPathAuthenticators != null && requestPathAuthenticators.length > 0) {
            List<RequestPathAuthenticatorConfig> reqAuthList = new ArrayList<RequestPathAuthenticatorConfig>();
            for (String name : requestPathAuthenticators) {
                if (name != null) {
                    RequestPathAuthenticatorConfig reqAuth = new RequestPathAuthenticatorConfig();
                    reqAuth.setName(name);
                    reqAuth.setDisplayName(request.getParameter("req_path_auth_" + name));
                    reqAuthList.add(reqAuth);
                }
            }

            if (reqAuthList.size() > 0) {
                serviceProvider.setRequestPathAuthenticatorConfigs(reqAuthList
                        .toArray(new RequestPathAuthenticatorConfig[reqAuthList.size()]));
            } else {
                serviceProvider.setRequestPathAuthenticatorConfigs(null);
            }
        } else {
            serviceProvider.setRequestPathAuthenticatorConfigs(null);
        }

        List<InboundAuthenticationRequestConfig> authRequestList = new ArrayList<InboundAuthenticationRequestConfig>();

        // update in-bound authentication configuration.

        if (samlIssuer != null) {
            InboundAuthenticationRequestConfig samlAuthenticationRequest = new InboundAuthenticationRequestConfig();
            samlAuthenticationRequest.setInboundAuthKey(samlIssuer);
            samlAuthenticationRequest.setInboundAuthType("samlsso");
            if (attrConsumServiceIndex != null && !attrConsumServiceIndex.isEmpty()) {
                Property property = new Property();
                property.setName("attrConsumServiceIndex");
                property.setValue(attrConsumServiceIndex);
                Property[] properties = {property};
                samlAuthenticationRequest.setProperties(properties);
            }
            authRequestList.add(samlAuthenticationRequest);
        }

        if (oauthAppName != null) {
            InboundAuthenticationRequestConfig opicAuthenticationRequest = new InboundAuthenticationRequestConfig();
            opicAuthenticationRequest.setInboundAuthKey(oauthAppName);
            opicAuthenticationRequest.setInboundAuthType("oauth2");
            if (oauthConsumerSecret != null && !oauthConsumerSecret.isEmpty()) {
                Property property = new Property();
                property.setName("oauthConsumerSecret");
                property.setValue(oauthConsumerSecret);
                Property[] properties = {property};
                opicAuthenticationRequest.setProperties(properties);
            }
            authRequestList.add(opicAuthenticationRequest);
        }

        if (wstrustEp != null) {
            InboundAuthenticationRequestConfig opicAuthenticationRequest = new InboundAuthenticationRequestConfig();
            opicAuthenticationRequest.setInboundAuthKey(wstrustEp);
            opicAuthenticationRequest.setInboundAuthType("wstrust");
            authRequestList.add(opicAuthenticationRequest);
        }

        String passiveSTSRealm = CharacterEncoder.getSafeText(request.getParameter("passiveSTSRealm"));
        String passiveSTSWReply = CharacterEncoder.getSafeText(request.getParameter("passiveSTSWReply"));

        if (passiveSTSRealm != null) {
            InboundAuthenticationRequestConfig opicAuthenticationRequest = new InboundAuthenticationRequestConfig();
            opicAuthenticationRequest.setInboundAuthKey(passiveSTSRealm);
            opicAuthenticationRequest.setInboundAuthType("passivests");
            if (passiveSTSWReply != null && !passiveSTSWReply.isEmpty()) {
                Property property = new Property();
                property.setName("passiveSTSWReply");
                property.setValue(passiveSTSWReply);
                Property[] properties = { property };
                opicAuthenticationRequest.setProperties(properties);
            }
            authRequestList.add(opicAuthenticationRequest);
        }

        String openidRealm = CharacterEncoder.getSafeText(request.getParameter("openidRealm"));

        if (openidRealm != null) {
            InboundAuthenticationRequestConfig opicAuthenticationRequest = new InboundAuthenticationRequestConfig();
            opicAuthenticationRequest.setInboundAuthKey(openidRealm);
            opicAuthenticationRequest.setInboundAuthType("openid");
            authRequestList.add(opicAuthenticationRequest);
        }

        if (serviceProvider.getInboundAuthenticationConfig() == null) {
            serviceProvider.setInboundAuthenticationConfig(new InboundAuthenticationConfig());
        }

        if (authRequestList.size() > 0) {
            serviceProvider.getInboundAuthenticationConfig()
                    .setInboundAuthenticationRequestConfigs(
                            authRequestList
                                    .toArray(new InboundAuthenticationRequestConfig[authRequestList
                                            .size()]));
        }

        // update local and out-bound authentication.
        if (AUTH_TYPE_DEFAULT.equalsIgnoreCase(serviceProvider
                .getLocalAndOutBoundAuthenticationConfig().getAuthenticationType())) {
            serviceProvider.getLocalAndOutBoundAuthenticationConfig().setAuthenticationSteps(null);
        } else if (AUTH_TYPE_LOCAL.equalsIgnoreCase(serviceProvider
                .getLocalAndOutBoundAuthenticationConfig().getAuthenticationType())) {
            AuthenticationStep authStep = new AuthenticationStep();
            LocalAuthenticatorConfig localAuthenticator = new LocalAuthenticatorConfig();
            localAuthenticator.setName(CharacterEncoder.getSafeText(request.getParameter("local_authenticator")));
            if (localAuthenticator.getName() != null && localAuthenticatorConfigs != null) {
                for (LocalAuthenticatorConfig config : localAuthenticatorConfigs) {
                    if (config.getName().equals(localAuthenticator.getName())) {
                        localAuthenticator.setDisplayName(config.getDisplayName());
                        break;
                    }
                }
            }
            authStep.setLocalAuthenticatorConfigs(new LocalAuthenticatorConfig[]{localAuthenticator});
            serviceProvider.getLocalAndOutBoundAuthenticationConfig().setAuthenticationSteps(
                    new AuthenticationStep[]{authStep});
        } else if (AUTH_TYPE_FEDERATED.equalsIgnoreCase(serviceProvider
                .getLocalAndOutBoundAuthenticationConfig().getAuthenticationType())) {
            AuthenticationStep authStep = new AuthenticationStep();
            IdentityProvider idp = new IdentityProvider();
            idp.setIdentityProviderName(CharacterEncoder.getSafeText(request.getParameter("fed_idp")));
            authStep.setFederatedIdentityProviders(new IdentityProvider[]{idp});
            serviceProvider.getLocalAndOutBoundAuthenticationConfig().setAuthenticationSteps(
                    new AuthenticationStep[]{authStep});
        } else if (AUTH_TYPE_FLOW.equalsIgnoreCase(serviceProvider
                .getLocalAndOutBoundAuthenticationConfig().getAuthenticationType())) {
            // already updated.
        }

        String alwaysSendAuthListOfIdPs = CharacterEncoder.getSafeText(request.getParameter("always_send_auth_list_of_idps"));
        serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                .setAlwaysSendBackAuthenticatedListOfIdPs(alwaysSendAuthListOfIdPs != null &&
                        "on".equals(alwaysSendAuthListOfIdPs) ? true : false);


        String subjectClaimUri = CharacterEncoder.getSafeText(request.getParameter("subject_claim_uri"));
        serviceProvider.getLocalAndOutBoundAuthenticationConfig()
                .setSubjectClaimUri((subjectClaimUri != null && !subjectClaimUri.isEmpty()) ? subjectClaimUri : null);

        // update application permissions.
        PermissionsAndRoleConfig permAndRoleConfig = new PermissionsAndRoleConfig();
        String[] permissions = request.getParameterValues("app_permission");
        List<ApplicationPermission> appPermList = new ArrayList<ApplicationPermission>();

        if (permissions != null && permissions.length > 0) {
            for (String permission : permissions) {
                if (permission != null && !permission.trim().isEmpty()) {
                    ApplicationPermission appPermission = new ApplicationPermission();
                    appPermission.setValue(permission);
                    appPermList.add(appPermission);
                }
            }
        }

        if (appPermList.size() > 0) {
            permAndRoleConfig.setPermissions(appPermList
                    .toArray(new ApplicationPermission[appPermList.size()]));
        }

        // update role mapping.
        int roleMappingCount = Integer.parseInt(request.getParameter("number_of_rolemappings"));
        List<RoleMapping> roleMappingList = new ArrayList<RoleMapping>();

        for (int i = 0; i < roleMappingCount; i++) {
            RoleMapping mapping = new RoleMapping();
            LocalRole localRole = new LocalRole();
            localRole.setLocalRoleName(CharacterEncoder.getSafeText(request.getParameter("idpRole_" + i)));
            mapping.setLocalRole(localRole);
            mapping.setRemoteRole(CharacterEncoder.getSafeText(request.getParameter("spRole_" + i)));
            if (mapping.getLocalRole() != null && mapping.getRemoteRole() != null) {
                roleMappingList.add(mapping);
            }
        }

        permAndRoleConfig.setRoleMappings(roleMappingList.toArray(new RoleMapping[roleMappingList
                .size()]));
        serviceProvider.setPermissionAndRoleConfig(permAndRoleConfig);

        if (serviceProvider.getClaimConfig() == null) {
            serviceProvider.setClaimConfig(new ClaimConfig());
        }

        if (request.getParameter("claim_dialect") != null
                && request.getParameter("claim_dialect").equals("custom")) {
            serviceProvider.getClaimConfig().setLocalClaimDialect(false);
        } else {
            serviceProvider.getClaimConfig().setLocalClaimDialect(true);
        }

        // update claim configuration.
        int claimCount = Integer.parseInt(request.getParameter("number_of_claimmappings"));
        List<ClaimMapping> claimMappingList = new ArrayList<ClaimMapping>();

        for (int i = 0; i < claimCount; i++) {
            ClaimMapping mapping = new ClaimMapping();

            Claim localClaim = new Claim();
            localClaim.setClaimUri(CharacterEncoder.getSafeText(request.getParameter("idpClaim_" + i)));

            Claim spClaim = new Claim();
            spClaim.setClaimUri(CharacterEncoder.getSafeText(request.getParameter("spClaim_" + i)));

            String requested = CharacterEncoder.getSafeText(request.getParameter("spClaim_req_" + i));
            if (requested != null && "on".equals(requested)) {
                mapping.setRequested(true);
            } else {
                mapping.setRequested(false);
            }

            mapping.setLocalClaim(localClaim);
            mapping.setRemoteClaim(spClaim);

            if (isLocalClaimsSelected() || mapping.getRemoteClaim().getClaimUri() == null ||
                    mapping.getRemoteClaim().getClaimUri().isEmpty()) {
                mapping.getRemoteClaim().setClaimUri(mapping.getLocalClaim().getClaimUri());
            }

            if (mapping.getLocalClaim().getClaimUri() != null
                    && mapping.getRemoteClaim().getClaimUri() != null) {
                claimMappingList.add(mapping);
            }
        }

        serviceProvider.getClaimConfig().setClaimMappings(
                claimMappingList.toArray(new ClaimMapping[claimMappingList.size()]));

        serviceProvider.getClaimConfig().setRoleClaimURI(CharacterEncoder.getSafeText(request.getParameter("roleClaim")));

        String alwaysSendMappedLocalSubjectId = CharacterEncoder.getSafeText(request
                .getParameter("always_send_local_subject_id"));
        serviceProvider.getClaimConfig().setAlwaysSendMappedLocalSubjectId(
                alwaysSendMappedLocalSubjectId != null
                        && "on".equals(alwaysSendMappedLocalSubjectId) ? true : false);

    }

    /**
     * @return
     */
    public Map<String, String> getRequestedClaims() {
        return requestedClaims;
    }

    /**
     * @param wstrustEp
     */
    public void setWstrustEp(String wstrustEp) {
        this.wstrustEp = wstrustEp;
    }

    /**
     * @param passivests
     */
    public void setPassivests(String passivests) {
        this.passivests = passivests;
    }

    /**
     *
     * @param passiveSTSWReply
     */
    public void setPassiveSTSWReply(String passiveSTSWReply) {
        this.passiveSTSWReply = passiveSTSWReply;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @param request
     */
    public void updateLocalSp(HttpServletRequest request) {

        // update basic info.
        serviceProvider.setApplicationName(CharacterEncoder.getSafeText(request.getParameter("spName")));
        serviceProvider.setDescription(CharacterEncoder.getSafeText(request.getParameter("sp-description")));

        String provisioningUserStore = CharacterEncoder.getSafeText(request.getParameter("scim-inbound-userstore"));
        InboundProvisioningConfig inBoundProConfig = new InboundProvisioningConfig();
        inBoundProConfig.setProvisioningUserStore(provisioningUserStore);
        serviceProvider.setInboundProvisioningConfig(inBoundProConfig);

        String[] provisioningProviders = request.getParameterValues("provisioning_idp");
        List<IdentityProvider> provisioningIdps = new ArrayList<IdentityProvider>();

        if (serviceProvider.getOutboundProvisioningConfig() == null
                || provisioningProviders == null || provisioningProviders.length == 0) {
            serviceProvider.setOutboundProvisioningConfig(new OutboundProvisioningConfig());
        }

        if (provisioningProviders != null && provisioningProviders.length > 0) {
            for (String proProvider : provisioningProviders) {
                String connector = CharacterEncoder.getSafeText(request.getParameter("provisioning_con_idp_" + proProvider));
                String jitEnabled = CharacterEncoder.getSafeText(request.getParameter("provisioning_jit_" + proProvider));
                String blocking = CharacterEncoder.getSafeText(request.getParameter("blocking_prov_" + proProvider));

                JustInTimeProvisioningConfig jitpro = new JustInTimeProvisioningConfig();

                if ("on".equals(jitEnabled)) {
                    jitpro.setProvisioningEnabled(true);
                }


                if (connector != null) {
                    IdentityProvider proIdp = new IdentityProvider();
                    proIdp.setIdentityProviderName(proProvider);
                    ProvisioningConnectorConfig proCon = new ProvisioningConnectorConfig();
                    if ("on".equals(blocking)) {
                        proCon.setBlocking(true);
                    }
                    proCon.setName(connector);
                    proIdp.setJustInTimeProvisioningConfig(jitpro);
                    proIdp.setDefaultProvisioningConnectorConfig(proCon);
                    provisioningIdps.add(proIdp);
                }
            }

            if (provisioningIdps.size() > 0) {
                OutboundProvisioningConfig outboundProConfig = new OutboundProvisioningConfig();
                outboundProConfig.setProvisioningIdentityProviders(provisioningIdps
                        .toArray(new IdentityProvider[provisioningIdps.size()]));
                serviceProvider.setOutboundProvisioningConfig(outboundProConfig);
            }
        }

    }
}