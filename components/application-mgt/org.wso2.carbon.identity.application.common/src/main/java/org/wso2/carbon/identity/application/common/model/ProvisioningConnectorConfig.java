/*
 *Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.application.common.model;

import org.apache.axiom.om.OMElement;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationManagementUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ProvisioningConnectorConfig implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8270617885506096420L;

    protected Property[] provisioningProperties = new Property[0];
    protected String name;
    protected boolean enabled;
    protected boolean blocking;

    /*
         * <ProvisioningConnectorConfig> <Name></Name> <ProvisioningProperties></ProvisioningProperties>
         * </ProvisioningConnectorConfig>
         */
    public static ProvisioningConnectorConfig build(OMElement provisioningConnectorConfigOM) {
        ProvisioningConnectorConfig provisioningConnectorConfig = new ProvisioningConnectorConfig();

        Iterator<?> iter = provisioningConnectorConfigOM.getChildElements();

        while (iter.hasNext()) {
            OMElement element = (OMElement) (iter.next());
            String elementName = element.getLocalName();

            if (elementName.equals("ProvisioningProperties")) {
                Iterator<?> propertiesIter = element.getChildElements();
                ArrayList<Property> propertiesArrList = new ArrayList<Property>();

                if (propertiesIter != null) {
                    while (propertiesIter.hasNext()) {
                        OMElement propertiesElement = (OMElement) (propertiesIter.next());
                        propertiesArrList.add(Property.build(propertiesElement));
                    }
                }

                if (propertiesArrList.size() > 0) {
                    Property[] propertiesArr = propertiesArrList.toArray(new Property[0]);
                    provisioningConnectorConfig.setProvisioningProperties(propertiesArr);
                }
            }

            if (elementName.equals("Name")) {
                provisioningConnectorConfig.setName(element.getText());
            }
        }

        return provisioningConnectorConfig;
    }

    /**
     * @return
     */
    public Property[] getProvisioningProperties() {
        return provisioningProperties;
    }

    /**
     * @param provisioningProperties
     */
    public void setProvisioningProperties(Property[] provisioningProperties) {
        if (this.provisioningProperties != null && this.provisioningProperties.length > 0
                && provisioningProperties != null) {
            this.provisioningProperties = IdentityApplicationManagementUtil.concatArrays(
                    this.provisioningProperties, provisioningProperties);
        } else {
            this.provisioningProperties = provisioningProperties;
        }

    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param string
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public boolean isValid() {
        return false;
    }

    /**
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param string
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProvisioningConnectorConfig)) return false;

        ProvisioningConnectorConfig that = (ProvisioningConnectorConfig) o;

        if (!StringUtils.equals(name, that.name)) return false;
        if (!Arrays.equals(provisioningProperties, that.provisioningProperties)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = hashCode * 17 + (name != null ? name.hashCode() : 0);
        hashCode = hashCode * 31 + (provisioningProperties != null ? Arrays.hashCode(provisioningProperties) : 0);
        return hashCode;
    }
}
