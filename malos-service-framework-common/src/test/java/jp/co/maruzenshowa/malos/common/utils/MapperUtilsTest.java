package jp.co.maruzenshowa.malos.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jp.co.maruzenshowa.malos.common.dto.MapperFromDto;
import jp.co.maruzenshowa.malos.common.dto.MapperToDto;

@ExtendWith(SpringExtension.class)
public class MapperUtilsTest {
	@Test
	void testMapingFromTo() {
		MapperFromDto from = new MapperFromDto();
		from.setPartName("part");
		from.setRequired(true);
		from.setName("from");
		from.setNone(null);
		from.setOtherFrom("otherFrom");
		
		MapperToDto to = new MapperToDto();
		to.setNone("none");
		MapperUtils.modelMapper().map(from, to);
		assertEquals(null, to.getRequired());
		assertEquals("from", to.getName());
		assertEquals("none", to.getNone());
		assertEquals(null, to.getOtherTo());
		assertEquals(null, to.getPart());
	}
	
	@Test
	void testMapingFromToDefault() {
		MapperFromDto from = new MapperFromDto();
		from.setPartName("part");
		from.setRequired(true);
		from.setName("from");
		from.setNone(null);
		from.setOtherFrom("otherFrom");
		MapperToDto to = (new ModelMapper()).map(from, MapperToDto.class);
		assertEquals("true", to.getRequired());
		assertEquals("from", to.getName());
		assertEquals(null, to.getNone());
		assertEquals(null, to.getOtherTo());
		assertEquals("part", to.getPart());
	}
}
