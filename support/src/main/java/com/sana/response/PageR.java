package com.sana.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageR<T> implements Serializable {
    private List<T> list;
    // 基本类默认值0，包装类为null
    private long total;
}
