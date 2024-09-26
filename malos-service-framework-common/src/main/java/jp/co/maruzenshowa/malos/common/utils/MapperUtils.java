package jp.co.maruzenshowa.malos.common.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.AbstractCondition;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

/**
 * DTOとエンティティ変換ユーティリティクラス.<br>
 * DB MapperのDTOから、サービスDTOへ変換する機能を提供する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Component
public class MapperUtils {
	
	/**
	 * 変換マッピングを初期作成する.<br>
	 * 
	 * @return マップ
	 */
	public static ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setFullTypeMatchingRequired(true);
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		
		return modelMapper;
	}
	
	/**
	 * ブランクをスキップするCondition.<br>
	 * 
	 * @return マップ
	 */
	public static AbstractCondition<Object, Object> getSkipBlankCondition() {
		class SkipBlankCondition extends AbstractCondition<Object, Object> {
			@Override
			public boolean applies(MappingContext<Object, Object> context) {
				if (context.getSource() instanceof String) {
					return null != context.getSource() && !StringUtils.isBlank(context.getSource().toString());
				} else {
					return context.getSource() != null;
				}
			}
		}
		return new SkipBlankCondition();
	}
	
	/**
	 * DB MapperのDTOから、サービスDTOへ変換する.
	 * 
	 * @param <S> 変換元データタイプ
	 * @param <T> 変換先データタイプ
	 * @param source      変換元のDTOのリスト
	 * @param targetClass 変換先のクラス対象
	 * 
	 * @return 変換後のDTOのリスト
	 */
	public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		ModelMapper modelMapper = modelMapper();
		return source.stream().map(element -> modelMapper.map(element, targetClass)).toList();
	}
	
	/**
	 * DB MapperのDTOから、サービスDTOへ変換する.
	 * 
	 * @param <S> 変換元データタイプ
	 * @param <T> 変換先データタイプ
	 * @param source      変換元のDTOのリスト
	 * @param targetClass 変換先のクラス対象
	 * @param modelMapper 変換マッピング
	 * 
	 * @return 変換後のDTOのリスト
	 */
	public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass, ModelMapper modelMapper) {
		return source.stream().map(element -> modelMapper.map(element, targetClass)).toList();
	}
	
    /**
     * コンストラクタを提供.
     */
    private MapperUtils() {}
}
