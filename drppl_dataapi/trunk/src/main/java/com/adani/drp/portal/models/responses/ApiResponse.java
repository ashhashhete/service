package com.adani.drp.portal.models.responses;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiResponse {

	private Status status;
	private Object data;

	public ApiResponse(Status status, Object data) {
		this.status = status;
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public static class Status {

		public int status;
		public String code;
		public String message;

		public Status(int status, String code, String message) {
			super();
			this.status = status;
			this.code = code;
			this.message = message;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

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
}
