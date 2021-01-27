package com.excel.database.converter.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *     分页请求
 * </p>
 * @author zhangbin
 * @date 2019-04-29
 * @param <T>
 */
@ApiModel("分页请求")
@Data
public final class HttpPageRequest<T> implements Serializable {

    private static final long serialVersionUID = -6070105174598467241L;

    /**
	 * 页码。
	 */
	@ApiModelProperty(value = "页码")
	private Integer pageNum;

	/**
	 * 每页数据量。
	 */
	@ApiModelProperty(value = "每页数据量")
	private Integer pageSize;

	/**
	 * 查询参数。
	 */
	@ApiModelProperty(value = "查询参数")
	private T parameters;
}
