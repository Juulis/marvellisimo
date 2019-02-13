package malidaca.marvellisimo.models

class Character(
        val id: Int,
        val name: String,
        val description: String,
        val resourceURI: String,
        val thumbnail: Picture,
        val series: ItemList,
        val urls: Array<Url>
)
