package namelessnet.app.services

class LeafJNI {
    companion object {
        external fun leaf_run_with_options(rt_id: Short,
                                           config_path: String,
                                           auto_reload: Boolean,
                                           multi_thread: Boolean,
                                           auto_threads: Boolean,
                                           threads: Int,
                                           stack_size: Int): Int
        external fun leaf_run(rt_id: Short, config_path: String): Int
        external fun leaf_run_with_config_string(rt_id: Short, config: String): Int
        external fun leaf_reload(rt_id: Short): Int
        external fun leaf_shutdown(rt_id: Short): Boolean
        external fun leaf_test_config(config_path: String): Int
        init {
            System.loadLibrary("leaf") // Load the shared library that contains the Leaf functions
        }
    }
}