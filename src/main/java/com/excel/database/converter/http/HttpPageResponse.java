package com.excel.database.converter.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *     分页响应
 * </p>
 * @author zhangbin
 * @date 2019-04-29
 * @param <T>
 */
@ApiModel("分页请求返回")
@Data
final class HttpPageResponse<T> implements Serializable {

	private static final long serialVersionUID = 260121138972789764L;

	/**
	 * 数据集合。
	 */
	@ApiModelProperty(value = "数据集合")
	private T data;

	/**
	 * 总数据量。
	 */
    @ApiModelProperty(value = "总数据量")
	private Long totalElements;

	/**
	 * 总页数。
	 */
    @ApiModelProperty(value = "总页数")
	private Integer totalPages;

	/**
	 * 当前页码。
	 */
    @ApiModelProperty(value = "当前页码")
	private Integer pageNumber;

	/**
	 * 每页数据量。
	 */
    @ApiModelProperty(value = "每页数据量")
	private Integer pageSize;
}
