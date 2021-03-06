/**
 * 공통 Code 상수
 */
export class CommonCode {
	public SUCCESS: number = 200;
	public INVALID_REQUEST: number = 400;
	public UNAUTORIZED: number = 401;
	public FORBIDDEN: number = 403;
	public REQUEST_TIMEOUT: number = 408;
	public SERVER_ERROR: number = 500;
	public FAIL: number = -1;
	public RESULT_CODE: ResultCode = new ResultCode();
}

export class ResultCode {
	public SUCCESS: string = 'C0000';
	public FAIL: string = 'C9999';
}

export class Message {
  public SUCCESS: string = 'Completed successfully.';
  public ERROR: string = 'Unfortunately, failed.';
  public DELETED: string = 'Deleted successfully.';
  public CANCELD: string = 'Canceled successfully.';
  public COPIED: string = 'Copied successfully.';
}

/**
 * COMMON CONSTANT SUMMARY
 */
export class CommonConstant {
	// 공통 Code
	public static CODE: CommonCode = new CommonCode();
	public static MESSAGE: Message = new Message();
}
