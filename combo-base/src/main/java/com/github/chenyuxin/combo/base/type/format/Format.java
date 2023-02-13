package com.github.chenyuxin.combo.base.type.format;

import com.github.chenyuxin.combo.base.type.other.Parse;

public interface Format<T, E> extends Parse<E, T> {
	
   T getFormatValue(Object oValue);

}
