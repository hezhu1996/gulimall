<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="sku_id" prop="skuId">
      <el-input v-model="dataForm.skuId" placeholder="sku_id"></el-input>
    </el-form-item>
    <el-form-item label="spu_id" prop="spuId">
      <el-input v-model="dataForm.spuId" placeholder="spu_id"></el-input>
    </el-form-item>
    <el-form-item label="ÉÌÆ·Ãû×Ö" prop="spuName">
      <el-input v-model="dataForm.spuName" placeholder="ÉÌÆ·Ãû×Ö"></el-input>
    </el-form-item>
    <el-form-item label="»áÔ±êÇ³Æ" prop="memberNickName">
      <el-input v-model="dataForm.memberNickName" placeholder="»áÔ±êÇ³Æ"></el-input>
    </el-form-item>
    <el-form-item label="ÐÇ¼¶" prop="star">
      <el-input v-model="dataForm.star" placeholder="ÐÇ¼¶"></el-input>
    </el-form-item>
    <el-form-item label="»áÔ±ip" prop="memberIp">
      <el-input v-model="dataForm.memberIp" placeholder="»áÔ±ip"></el-input>
    </el-form-item>
    <el-form-item label="´´½¨Ê±¼ä" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="´´½¨Ê±¼ä"></el-input>
    </el-form-item>
    <el-form-item label="ÏÔÊ¾×´Ì¬[0-²»ÏÔÊ¾£¬1-ÏÔÊ¾]" prop="showStatus">
      <el-input v-model="dataForm.showStatus" placeholder="ÏÔÊ¾×´Ì¬[0-²»ÏÔÊ¾£¬1-ÏÔÊ¾]"></el-input>
    </el-form-item>
    <el-form-item label="¹ºÂòÊ±ÊôÐÔ×éºÏ" prop="spuAttributes">
      <el-input v-model="dataForm.spuAttributes" placeholder="¹ºÂòÊ±ÊôÐÔ×éºÏ"></el-input>
    </el-form-item>
    <el-form-item label="µãÔÞÊý" prop="likesCount">
      <el-input v-model="dataForm.likesCount" placeholder="µãÔÞÊý"></el-input>
    </el-form-item>
    <el-form-item label="»Ø¸´Êý" prop="replyCount">
      <el-input v-model="dataForm.replyCount" placeholder="»Ø¸´Êý"></el-input>
    </el-form-item>
    <el-form-item label="ÆÀÂÛÍ¼Æ¬/ÊÓÆµ[jsonÊý¾Ý£»[{type:ÎÄ¼þÀàÐÍ,url:×ÊÔ´Â·¾¶}]]" prop="resources">
      <el-input v-model="dataForm.resources" placeholder="ÆÀÂÛÍ¼Æ¬/ÊÓÆµ[jsonÊý¾Ý£»[{type:ÎÄ¼þÀàÐÍ,url:×ÊÔ´Â·¾¶}]]"></el-input>
    </el-form-item>
    <el-form-item label="ÄÚÈÝ" prop="content">
      <el-input v-model="dataForm.content" placeholder="ÄÚÈÝ"></el-input>
    </el-form-item>
    <el-form-item label="ÓÃ»§Í·Ïñ" prop="memberIcon">
      <el-input v-model="dataForm.memberIcon" placeholder="ÓÃ»§Í·Ïñ"></el-input>
    </el-form-item>
    <el-form-item label="ÆÀÂÛÀàÐÍ[0 - ¶ÔÉÌÆ·µÄÖ±½ÓÆÀÂÛ£¬1 - ¶ÔÆÀÂÛµÄ»Ø¸´]" prop="commentType">
      <el-input v-model="dataForm.commentType" placeholder="ÆÀÂÛÀàÐÍ[0 - ¶ÔÉÌÆ·µÄÖ±½ÓÆÀÂÛ£¬1 - ¶ÔÆÀÂÛµÄ»Ø¸´]"></el-input>
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
          skuId: '',
          spuId: '',
          spuName: '',
          memberNickName: '',
          star: '',
          memberIp: '',
          createTime: '',
          showStatus: '',
          spuAttributes: '',
          likesCount: '',
          replyCount: '',
          resources: '',
          content: '',
          memberIcon: '',
          commentType: ''
        },
        dataRule: {
          skuId: [
            { required: true, message: 'sku_id不能为空', trigger: 'blur' }
          ],
          spuId: [
            { required: true, message: 'spu_id不能为空', trigger: 'blur' }
          ],
          spuName: [
            { required: true, message: 'ÉÌÆ·Ãû×Ö不能为空', trigger: 'blur' }
          ],
          memberNickName: [
            { required: true, message: '»áÔ±êÇ³Æ不能为空', trigger: 'blur' }
          ],
          star: [
            { required: true, message: 'ÐÇ¼¶不能为空', trigger: 'blur' }
          ],
          memberIp: [
            { required: true, message: '»áÔ±ip不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '´´½¨Ê±¼ä不能为空', trigger: 'blur' }
          ],
          showStatus: [
            { required: true, message: 'ÏÔÊ¾×´Ì¬[0-²»ÏÔÊ¾£¬1-ÏÔÊ¾]不能为空', trigger: 'blur' }
          ],
          spuAttributes: [
            { required: true, message: '¹ºÂòÊ±ÊôÐÔ×éºÏ不能为空', trigger: 'blur' }
          ],
          likesCount: [
            { required: true, message: 'µãÔÞÊý不能为空', trigger: 'blur' }
          ],
          replyCount: [
            { required: true, message: '»Ø¸´Êý不能为空', trigger: 'blur' }
          ],
          resources: [
            { required: true, message: 'ÆÀÂÛÍ¼Æ¬/ÊÓÆµ[jsonÊý¾Ý£»[{type:ÎÄ¼þÀàÐÍ,url:×ÊÔ´Â·¾¶}]]不能为空', trigger: 'blur' }
          ],
          content: [
            { required: true, message: 'ÄÚÈÝ不能为空', trigger: 'blur' }
          ],
          memberIcon: [
            { required: true, message: 'ÓÃ»§Í·Ïñ不能为空', trigger: 'blur' }
          ],
          commentType: [
            { required: true, message: 'ÆÀÂÛÀàÐÍ[0 - ¶ÔÉÌÆ·µÄÖ±½ÓÆÀÂÛ£¬1 - ¶ÔÆÀÂÛµÄ»Ø¸´]不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/product/spucomment/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.skuId = data.spuComment.skuId
                this.dataForm.spuId = data.spuComment.spuId
                this.dataForm.spuName = data.spuComment.spuName
                this.dataForm.memberNickName = data.spuComment.memberNickName
                this.dataForm.star = data.spuComment.star
                this.dataForm.memberIp = data.spuComment.memberIp
                this.dataForm.createTime = data.spuComment.createTime
                this.dataForm.showStatus = data.spuComment.showStatus
                this.dataForm.spuAttributes = data.spuComment.spuAttributes
                this.dataForm.likesCount = data.spuComment.likesCount
                this.dataForm.replyCount = data.spuComment.replyCount
                this.dataForm.resources = data.spuComment.resources
                this.dataForm.content = data.spuComment.content
                this.dataForm.memberIcon = data.spuComment.memberIcon
                this.dataForm.commentType = data.spuComment.commentType
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
              url: this.$http.adornUrl(`/product/spucomment/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'skuId': this.dataForm.skuId,
                'spuId': this.dataForm.spuId,
                'spuName': this.dataForm.spuName,
                'memberNickName': this.dataForm.memberNickName,
                'star': this.dataForm.star,
                'memberIp': this.dataForm.memberIp,
                'createTime': this.dataForm.createTime,
                'showStatus': this.dataForm.showStatus,
                'spuAttributes': this.dataForm.spuAttributes,
                'likesCount': this.dataForm.likesCount,
                'replyCount': this.dataForm.replyCount,
                'resources': this.dataForm.resources,
                'content': this.dataForm.content,
                'memberIcon': this.dataForm.memberIcon,
                'commentType': this.dataForm.commentType
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
