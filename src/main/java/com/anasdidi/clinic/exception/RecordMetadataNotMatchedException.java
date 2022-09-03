package com.anasdidi.clinic.exception;

import com.anasdidi.clinic.common.BaseException;

import lombok.Getter;

@Getter
public class RecordMetadataNotMatchedException extends BaseException {

  private final String dbId;
  private final Long dbVersion;
  private final String reqId;
  private final Long reqVersion;

  public RecordMetadataNotMatchedException(String traceId, String dbId, long dbVersion, String reqId, Long reqVersion) {
    super(traceId);
    this.dbId = dbId;
    this.dbVersion = dbVersion;
    this.reqId = reqId;
    this.reqVersion = reqVersion;
  }

  @Override
  public String getMessage() {
    return "traceId=%s, [db, req]: id:[%s, %s], version:[%s, %s]".formatted(traceId, dbId, reqId, dbVersion,
        reqVersion);
  }
}
