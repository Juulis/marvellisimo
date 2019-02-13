package malidaca.marvellisimo.models

class Series(val id: Int, val title: String, val description: String, val resourceURI: String,
             val startYear: Int, val endYear: Int, val rating: String, val thumbnail: Picture,
             val creators: ItemList, val characters: ItemList, val comics: ItemList,
             val urls: Array<Url>)