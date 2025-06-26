/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.plugins.notifygru.modules.forms.services.provider;

public class Constants {
	
	
	  // PROPERTY KEY
    public static final String PROPERTY_SMS_SENDER_NAME = "workflow-notifygruforms.gruprovider.sms.sendername";
    public static final String ALL_FORMS = "*";

    // PARAMETERS
    public static final String PARAMETER_VIEW_FORM_RESPONSE_DETAILS = "view_form_response_details";
    public static final String PARAMETER_ID_FORM_RESPONSES = "id_form_response";
    
    // MARKS
    public static final String MARK_POSITION = "position_";
    public static final String MARK_POSITION_ITERATION = "_";
    public static final String MARK_URL_ADMIN_RESPONSE = "url_admin_forms_response_detail";
	
    // Messages
    public static final String TITLE_I18NKEY = "module.notifygru.forms.module.providerdirectory";
    public static final String DESCRIPTION_ALLFORMS_SUFFIX = "All Forms";
    public static final String MESSAGE_DESCRIPTION = "forms.marker.provider.url.admin.detail.reponse.description";

    public static final String MARK_CREATION_DATE = "creation_date";
    public static final String MESSAGE_CREATION_DATE = "forms.marker.provider.url.detail.reponse.creation_date";
    public static final String MARK_CREATION_TIME = "creation_time";
    public static final String MESSAGE_CREATION_TIME = "forms.marker.provider.url.detail.reponse.creation_time";

    public static final String MARK_UPDATE_TIME = "update_time";
    public static final String MESSAGE_UPDATE_TIME = "forms.marker.provider.url.detail.reponse.update_time";
    public static final String MARK_UPDATE_DATE = "update_date";
    public static final String MESSAGE_UPDATE_DATE = "forms.marker.provider.url.detail.reponse.update_date";
	private Constants() {}

}
