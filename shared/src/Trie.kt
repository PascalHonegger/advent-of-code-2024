/**
 * Inspired by https://1gravityllc.medium.com/trie-kotlin-50d8ae041202
 */
class Trie<Value> {
    data class Node<Value>(
        val children: MutableMap<Char, Node<Value>> = mutableMapOf(),
        var value: Value? = null,
    )

    val root = Node<Value>()

    fun insert(key: String, value: Value, node: Node<Value> = root) {
        key.fold(node) { node, char ->
            node.children[char] ?: Node<Value>().also { node.children[char] = it }
        }.value = value
    }

    fun search(key: String, node: Node<Value> = root) = key.fold(node) { node, char ->
        node.children[char] ?: return null
    }.value

    fun delete(key: String) {
        delete(key, 0, root)
    }

    private fun delete(key: String, index: Int, node: Node<Value>) {
        if (index == key.length)
            node.value = null
        else node.children[key[index]]?.run {
            delete(key, index + 1, this)
            if (children.isEmpty() && value == null) node.children.remove(key[index])
        }
    }
}