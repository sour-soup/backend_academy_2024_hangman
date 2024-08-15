package backend.academy.samples.mapstruct;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {
    public static void main(String[] args) {
        var mapper = new CarMapperImpl();

        CarDto volvo = mapper.carToCarDto(new Car("Volvo", 4));
        log.info(volvo);
        PersonDto person = mapper.personToPersonDto(new Person("Alexander Biryukov"));
        log.info(person);

    }
}
