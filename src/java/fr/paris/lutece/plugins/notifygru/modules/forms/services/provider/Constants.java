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
    
    public static final String MARK_QRCODE_URL_BO = "url_bo_forms_qrcode";
    public static final String MESSAGE_URL_QR_CODE_BO = "forms.marker.provider.url.admin.detail.reponse.urlqrcode";
    public static final String MARK_QRCODE_URL_FO = "url_fo_forms_qrcode";
    public static final String MESSAGE_URL_QR_CODE_FO = "forms.marker.provider.url.fo.detail.reponse.urlqrcode";
	private Constants() {}

}
