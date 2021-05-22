package itis.parsing;

interface ParkParsingService {

    Park parseParkData(String parkDatafilePath) throws ParkParsingException;

}
