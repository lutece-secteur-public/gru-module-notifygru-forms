/*
 * Copyright (c) 2002-2018, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.notifygru.modules.forms.services;


import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.modulenotifygrumappingmanager.business.NotifygruMappingManager;
import java.util.List;

/**
 * The Interface INotifyGruFormsService.
 */
public interface INotifyGruFormsService
{
    /**
     * Get the customer email
     * @param mapping
     *          The Notifygru Mapping manager
     * @param formResponse
     *          The form Response
     * @return the customer email as a string
     */
    String getEmail( NotifygruMappingManager mapping, FormResponse formResponse );

    /**
     * Get the id demand with the given formResponse
     * @param formResponse
     *          The form response
     * @return the id demand
     */
    int getIdDemand( FormResponse formResponse );

    /**
     * Get the demand type id with the given form response
     * @param mapping
     *          The mapping Manager
     * @return the demand type id
     */
    int getIdDemandType( NotifygruMappingManager mapping );

    /**
     * Get the SMSPhone number
     * @param mapping
     *          The mapping Manager
     * @param formResponse
     *          The form response
     * @return the SMS phone number
     */
    String getSMSPhoneNumber( NotifygruMappingManager mapping, FormResponse formResponse );

    /**
     * Get the connection id of the customer, with given NotifyGruMappingManager and given formResponse
     * @param mapping
     *              The mapping Manager
     * @param formResponse
     *              The form response
     * @return the customer connection id, as a String
     */
    String getConnectionId( NotifygruMappingManager mapping, FormResponse formResponse );

    /**
     * Get the connection id of the customer, with given NotifyGruMappingManager and given formResponse
     * @param mapping
     *              The mapping Manager
     * @param formResponse
     *              The form response
     * @return the customer connection id, as a String
     */
    String getCustomerId( NotifygruMappingManager mapping, FormResponse formResponse );
    
    /**
     * Get the demand reference, with given NotifyGruMappingManager and given formResponse 
     * @param mapping
     *             The mapping Manager
     * @param formResponse
     *              The form response
     * @return the demand reference as a String
     */
    String getDemandReference( NotifygruMappingManager mapping, FormResponse formResponse );
    
    /**
     * Get the list of the form question response, with given formResponse
     * @param formResponse the formResponse
     * @return the formQuestionResponses objects as a list.
     */
    List<FormQuestionResponse> getListFormQuestionResponse( FormResponse formResponse );
}
