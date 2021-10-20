package com.upgrad.bookmyconsultation.controller.ext;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * ErrorResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-02-02T16:46:24.234+05:30")
public class ErrorResponse   {
	private static final String INVALID_INPUTS = "invalid_inputs";

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("message")
	private String message = null;

	@JsonProperty("root_cause")
	private String rootCause = null;

	@JsonIgnore
	private Map<String, List<String>> invalidInputs = new HashMap<>();

	public ErrorResponse code(String code) {
		this.code = code;
		return this;
	}

	/**
	 * Application specific standard error code
	 * @return code
	 **/
	@ApiModelProperty(required = true, value = "Application specific standard error code")
	@NotNull


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ErrorResponse message(String message) {
		this.message = message;
		return this;
	}

	/**
	 * Error message stating the reason
	 * @return message
	 **/
	@ApiModelProperty(required = true, value = "Error message stating the reason")
	@NotNull


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ErrorResponse rootCause(String rootCause) {
		this.rootCause = rootCause;
		return this;
	}

	/**
	 * Root cause of the Error
	 * @return rootCause
	 **/
	@ApiModelProperty(value = "Root cause of the Error")


	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public ErrorResponse invalidInputs (List<String> invalidInputs) {
		this.invalidInputs.put(INVALID_INPUTS, invalidInputs);
		return this;
	}

	@JsonAnyGetter
	public Map<String, List<String>> getInvalidInputs () {
		return this.invalidInputs;
	}

	@JsonAnySetter
	public void setInvalidInputs (List<String> invalidInputs) {
		this.invalidInputs.put(INVALID_INPUTS, invalidInputs);
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ErrorResponse errorResponse = (ErrorResponse) o;
		return Objects.equals(this.code, errorResponse.code) &&
				Objects.equals(this.message, errorResponse.message) &&
				Objects.equals(this.rootCause, errorResponse.rootCause);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, message, rootCause);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ErrorResponse {\n");

		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    message: ").append(toIndentedString(message)).append("\n");
		sb.append("    rootCause: ").append(toIndentedString(rootCause)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

