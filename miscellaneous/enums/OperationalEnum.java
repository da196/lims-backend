/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tz.go.tcra.lims.miscellaneous.enums;

/**
 *
 * @author emmanuel.mfikwa
 */
public enum OperationalEnum {
    OnOperation, //applies to licences & application that are operational
    OnTransfer, //applies to licences that on transfer process
    OnRenewal, //applies to licences that are on renewal process
    OnCancellation, //applies to licences that are on cancellation process
    OnUpgrade, //applies to licences that are on upgrade process
    OnShareholderChange, //applies to entities that are on shareholder process
    OnEntityNameChange //applies to entities that are on name change process
}
