package com.example.Subject_system.contants;

public enum RtnCode {

	SUCCESSFUL("200", "Successful!!"),
	DATA_MISINPUT("404", "Misinput!!"),
	DATA_ERROR("404", "Data Error!!"),
	DATA_IS_EMPTY("404", "Data Not Found!!"),
	TAKE_COURSE_NOTYET("404", "Please take course, first!!"),
	UPDATE_NOT_ALLOW("404", "This Update Is Not Allow!!");
	
	private String code;
	
	private String message;
	
	//================
	
	private RtnCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	//================

	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
