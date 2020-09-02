const config = {
    container: "",
    url: "",
    style: ""
}

FileTool.prototype.load = function (e){


}
const FileTool = {

    /**
     * 加载文件
     * @param config config 配置
     */
    function loadFiles(config) {
        if(config == null){
            alert('无效的文件配置');
        }

        if(config.currentPage === null || config.currentPage < 0){
            config.currentPage = 1;
        }

        if(config.currentPage > 1){
            config.url = config.url + "?currentPage=" + config.currentPage;
        }

        if(config.style == null || config.style === ""){
            config.style = "bootstrap";
        }

        fetch(config.url, {
            method: 'GET'
        }).then(response => response.json())
            .then(response => {
                console.log('Success:', JSON.stringify(response));

                let fileContainer = document.getElementById(config.container);

                if(config.style === "bootstrap") {

                    console.log("active style:" + config.style);

                    let html = this.initFileTableHeader();

                    html = html + '<tbody>';

                    if (response.code === 200) {
                        const currentPage = response.data.currentPage;
                        const pageSize = response.data.pageSize;

                        const fileDataArray = response.data.data;

                        for (let i = 0; i < fileDataArray.length; i++) {
                            const fileData = fileDataArray[i];

                            html +=
                                '<tr>' +
                                '<td>' + fileData.fileName + '</td>' +
                                '<td>' + fileData.ext + '</td>' +
                                '<td>' + fileData.size + '</td>' +
                                '<td>' + fileData.canEdit + '</td>' +
                                '<td>' + fileData.directory + '</td>' +
                                '<td><a href="javascript:void(0);" data-edit="'+fileData.fileName+'">编辑</a></td>' +
                                '</tr>';
                        }
                        html += '</tbody>' + this.initFileTableFooter();
                        fileContainer.innerHTML = html;

                        for(const ele of document.querySelector('a[data-edit]')){

                            const fileName = this.dataset.edit;

                            ele.addEventListener('click', function(){
                                alert(fileName);
                            });
                        }

                    } else {

                    }
                }
            }).catch(error => {
                alert('获取数据错误');
        });

    },


    /**
     * 初始化文件头部
     * @returns {string} string
     */
    initFileTableHeader(){
        return '<table class="table table-bordered">' +
            '<thead>' +
            '<tr>' +
            '<th>文件名称</th>' +
            '<th>文件类型</th>' +
            '<th>文件大小</th>' +
            '<th>编辑</th>' +
            '<th>预览</th>' +
            '<th>操作</th>' +
            '</tr>' +
            '</thead>';
    },

    /**
     * 初始化文件尾部
     * @returns {string} string
     */
     initFileTableFooter(){
        return '</table>';
    }

}