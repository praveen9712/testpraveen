import React from 'react';
import SwaggerUI from "swagger-ui";
import 'swagger-ui/dist/swagger-ui.css';
import openapijson from "../target/openapi/openapi.json";

const accuroapiplugin = () => {
  return {
    components: {
      authorizeOperationBtn: () => null
    },
    wrapComponents: {
      JumpToPath: (Original, system) => props => {
        if (props.path === undefined) {
          return null;
        }

        let operation = props.specSelectors
        .paths()
        .getIn(props.path.slice(1));

        let hasAccuroPermissions =
            operation.get("x-accuroProviderPermissions") ||
            operation.get("x-accuroFeaturePermissions") ||
            operation.get("x-accuroRoles");
        return (
            <div>
              {hasAccuroPermissions ? (
                  <img
                      src="./target_24x24.png"
                      width="20"
                      height="20"
                  />
              ) : null}
            </div>
        );
      },
      parameters: (Original, system) => props => {
        let hasScopes =
            props.operation.get("security") &&
            props.operation
            .get("security")
            .get(0)
            .get("oauth2")
                ? true
                : false;
        let hasProviderPermissions = props.operation.get(
            "x-accuroProviderPermissions"
        )
            ? true
            : false;
        let hasFeaturePermissions = props.operation.get(
            "x-accuroFeaturePermissions"
        )
            ? true
            : false;

        let hasRolePermissions = props.operation.get(
            "x-accuroRoles"
        )
            ? true
            : false;
        return (
            <div>
              <div>
                <div className="opblock-section">
                  <div className="opblock-section-header">
                    <div className="tab-item active">
                      <h4>OAuth Scopes Required (If more than one scope
                        is present, use any one.)</h4>

                    </div>
                  </div>
                </div>
                <div className="opblock-description-wrapper">
                  {hasScopes ? (
                      <p>
                        {props.operation
                        .get("security")
                        .get(0)
                        .get("oauth2")
                        .join(" ")}
                      </p>
                  ) : (
                      <p>No scopes required</p>
                  )}
                </div>
                <div>
                  <div className="opblock-section-header">
                    <div className="tab-item active">
                      <h4>Accuro Roles Permissions</h4>
                    </div>
                  </div>
                </div>
                <div className="opblock-description-wrapper">
                  {hasRolePermissions ? (
                      <p>{props.operation.get("x-accuroRoles")}</p>
                  ) : (
                      <p>No Roles Permissions Required</p>
                  )}
                </div>
                <div className="opblock-section">
                  <div className="opblock-section-header">
                    <div className="tab-item active">
                      <h4>Accuro Provider Permissions
                        {hasProviderPermissions ? (props.operation.get("x-accuroProviderPermissions")
                        .get("logicalOperator")) : ''}</h4>

                    </div>
                  </div>
                </div>
                <div className="opblock-description-wrapper">
                  {hasProviderPermissions ? (
                      <div>
                        <table>
                          <thead>
                          <tr>
                            <th>Type</th>
                            <th>Access Level</th>
                            <th>Description</th>
                          </tr>
                          </thead>
                          <tbody>
                          {props.operation
                          .get("x-accuroProviderPermissions")
                          .get("permissions") ? (
                              props.operation
                              .get("x-accuroProviderPermissions")
                              .get("permissions")
                              .map((permission, index, array) => {
                                const permissionRows = [];
                                permissionRows.push(
                                    <tr>
                                      <td>{permission.get("type")}</td>
                                      <td>{permission.get("level")}</td>
                                      <td>{permission.get("description")}</td>
                                    </tr>
                                );
                                if (index < array.length - 1) {
                                  permissionRows.push(
                                      <tr>
                                        <td colSpan={3}>
                                          {props.operation
                                          .get("x-accuroProviderPermissions")
                                          .get("logicalOperator")}
                                        </td>
                                      </tr>
                                  );
                                }
                                return permissionRows;
                              })
                          ) : (
                              <tr>
                                <td>
                                  {props.operation
                                  .get("x-accuroProviderPermissions")
                                  .get("type")}
                                </td>
                                <td>
                                  {props.operation
                                  .get("x-accuroProviderPermissions")
                                  .get("level")}
                                </td>
                                <td>
                                  {props.operation
                                  .get("x-accuroProviderPermissions")
                                  .get("description")}
                                </td>
                              </tr>
                          )}
                          </tbody>
                        </table>
                      </div>
                  ) : (
                      <p>No Provider Permissions Required</p>
                  )}
                </div>
                <div>
                  <div className="opblock-section-header">
                    <div className="tab-item active">
                      <h4>Accuro Feature Permissions</h4>
                    </div>
                  </div>
                </div>
                <div className="opblock-description-wrapper">
                  {hasFeaturePermissions ? (
                      <p>{props.operation.get("x-accuroFeaturePermissions")}</p>
                  ) : (
                      <p>No Feature Permissions Required</p>
                  )}
                </div>
              </div>
              <Original {...props} />
            </div>
        );
      }
    }
  };
};
const DisableTryItOutPlugin = function() {
  return {
    statePlugins: {
      spec: {
        wrapSelectors: {
          allowTryItOutFor: () => () => false
        }
      }
    }
  }
};

SwaggerUI({
  dom_id: "#swagger-ui",
  spec: openapijson,
  plugins: [accuroapiplugin, DisableTryItOutPlugin]
});
