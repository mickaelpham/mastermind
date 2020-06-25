# frozen_string_literal: true

require 'json'
require 'ruby/services/differ'

event_bus = $vertx.event_bus

event_bus.consumer('ruby.services.differ') do |msg|
  puts msg.inspect
  puts "I have received a message: #{msg.body}"

  differ_result = Differ.call(name: msg.body)
  puts "differ result: #{differ_result}"

  msg.reply differ_result.to_json
end
