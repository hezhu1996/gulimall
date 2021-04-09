<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="Ãû×Ö" prop="name">
      <el-input v-model="dataForm.name" placeholder="Ãû×Ö"></el-input>
    </el-form-item>
    <el-form-item label="Í¼Æ¬µØÖ·" prop="pic">
      <el-input v-model="dataForm.pic" placeholder="Í¼Æ¬µØÖ·"></el-input>
    </el-form-item>
    <el-form-item label="¿ªÊ¼Ê±¼ä" prop="startTime">
      <el-input v-model="dataForm.startTime" placeholder="¿ªÊ¼Ê±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="½áÊøÊ±¼ä" prop="endTime">
      <el-input v-model="dataForm.endTime" placeholder="½áÊøÊ±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="×´Ì¬" prop="status">
      <el-input v-model="dataForm.status" placeholder="×´Ì¬"></el-input>
    </el-form-item>
    <el-form-item label="µã»÷Êý" prop="clickCount">
      <el-input v-model="dataForm.clickCount" placeholder="µã»÷Êý"></el-input>
    </el-form-item>
    <el-form-item label="¹ã¸æÏêÇéÁ¬½ÓµØÖ·" prop="url">
      <el-input v-model="dataForm.url" placeholder="¹ã¸æÏêÇéÁ¬½ÓµØÖ·"></el-input>
    </el-form-item>
    <el-form-item label="±¸×¢" prop="note">
      <el-input v-model="dataForm.note" placeholder="±¸×¢"></el-input>
    </el-form-item>
    <el-form-item label="ÅÅÐò" prop="sort">
      <el-input v-model="dataForm.sort" placeholder="ÅÅÐò"></el-input>
    </el-form-item>
    <el-form-item label="·¢²¼Õß" prop="publisherId">
      <el-input v-model="dataForm.publisherId" placeholder="·¢²¼Õß"></el-input>
    </el-form-item>
    <el-form-item label="ÉóºËÕß" prop="authId">
      <el-input v-model="dataForm.authId" placeholder="ÉóºËÕß"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          name: '',
          pic: '',
          startTime: '',
          endTime: '',
          status: '',
          clickCount: '',
          url: '',
          note: '',
          sort: '',
          publisherId: '',
          authId: ''
        },
        dataRule: {
          name: [
            { required: true, message: 'Ãû×Ö不能为空', trigger: 'blur' }
          ],
          pic: [
            { required: true, message: 'Í¼Æ¬µØÖ·不能为空', trigger: 'blur' }
          ],
          startTime: [
            { required: true, message: '¿ªÊ¼Ê±¼ä不能为空', trigger: 'blur' }
          ],
          endTime: [
            { required: true, message: '½áÊøÊ±¼ä不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '×´Ì¬不能为空', trigger: 'blur' }
          ],
          clickCount: [
            { required: true, message: 'µã»÷Êý不能为空', trigger: 'blur' }
          ],
          url: [
            { required: true, message: '¹ã¸æÏêÇéÁ¬½ÓµØÖ·不能为空', trigger: 'blur' }
          ],
          note: [
            { required: true, message: '±¸×¢不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: 'ÅÅÐò不能为空', trigger: 'blur' }
          ],
          publisherId: [
            { required: true, message: '·¢²¼Õß不能为空', trigger: 'blur' }
          ],
          authId: [
            { required: true, message: 'ÉóºËÕß不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/coupon/homeadv/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.homeAdv.name
                this.dataForm.pic = data.homeAdv.pic
                this.dataForm.startTime = data.homeAdv.startTime
                this.dataForm.endTime = data.homeAdv.endTime
                this.dataForm.status = data.homeAdv.status
                this.dataForm.clickCount = data.homeAdv.clickCount
                this.dataForm.url = data.homeAdv.url
                this.dataForm.note = data.homeAdv.note
                this.dataForm.sort = data.homeAdv.sort
                this.dataForm.publisherId = data.homeAdv.publisherId
                this.dataForm.authId = data.homeAdv.authId
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/coupon/homeadv/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'pic': this.dataForm.pic,
                'startTime': this.dataForm.startTime,
                'endTime': this.dataForm.endTime,
                'status': this.dataForm.status,
                'clickCount': this.dataForm.clickCount,
                'url': this.dataForm.url,
                'note': this.dataForm.note,
                'sort': this.dataForm.sort,
                'publisherId': this.dataForm.publisherId,
                'authId': this.dataForm.authId
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
