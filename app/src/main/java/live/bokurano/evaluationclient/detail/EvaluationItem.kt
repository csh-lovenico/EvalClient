package live.bokurano.evaluationclient.detail

data class TextItem(val id: Int, val title: String, val desc: String)

data class WithScore(val id: Int, val title: String, val desc: String, val score: Int)

