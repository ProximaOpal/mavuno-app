import React, { useState } from 'react';
import { View, Text, StyleSheet, Modal, TouchableOpacity, ScrollView, TextInput } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { colors, radii } from '../theme/theme';

export default function OverlayModal({ visible, type, onClose, onAction }) {
  const [chatInput, setChatInput] = useState('');
  const [messages, setMessages] = useState([
    { id: '1', text: 'Welcome to Mavuno Church! How can we support you today?', sender: 'bot', time: '10:28' },
  ]);

  const handleSendChat = () => {
    if (!chatInput.trim()) return;
    const userMsg = { id: String(Date.now()), text: chatInput, sender: 'user', time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) };
    setMessages((prev) => [...prev, userMsg]);
    setChatInput('');

    setTimeout(() => {
      const reply = {
        id: String(Date.now() + 1),
        text: 'Thank you for reaching out! A Mavuno pastor or leader will reply shortly. You are also welcome to join us this Sunday at 9:00 AM!',
        sender: 'bot',
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      };
      setMessages((prev) => [...prev, reply]);
    }, 1000);
  };

  if (!visible) return null;

  return (
    <Modal animationType="slide" transparent visible={visible} onRequestClose={onClose}>
      <TouchableOpacity style={styles.backdrop} activeOpacity={1} onPress={onClose}>
        <TouchableOpacity style={styles.modalContent} activeOpacity={1}>
          <View style={styles.header}>
            <View style={styles.headerTitleRow}>
              <View style={styles.iconCircle}>
                <Ionicons
                  name={
                    type === 'contact'
                      ? 'call'
                      : type === 'chat'
                      ? 'chatbubbles'
                      : type === 'video'
                      ? 'play'
                      : 'heart'
                  }
                  size={20}
                  color={colors.white}
                />
              </View>
              <Text style={styles.headerTitle}>
                {type === 'contact' && 'Contact Mavuno Campus'}
                {type === 'chat' && 'Live Support & Community Chat'}
                {type === 'video' && 'Latest Sermon — Watch Live'}
                {type === 'give' && 'Mavuno Giving & Tithes'}
              </Text>
            </View>
            <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
              <Ionicons name="close" size={20} color={colors.textSecondary} />
            </TouchableOpacity>
          </View>

          {/* CONTACT OVERLAY PANEL */}
          {type === 'contact' && (
            <ScrollView style={styles.body}>
              <Text style={styles.subtitle}>Get in touch with Mavuno Church Nairobi Campus</Text>
              
              <TouchableOpacity style={styles.contactCard} onPress={() => onAction?.('call')}>
                <Ionicons name="call-outline" size={24} color={colors.orange} />
                <View style={{ marginLeft: 16, flex: 1 }}>
                  <Text style={styles.cardTitle}>Campus Main Desk</Text>
                  <Text style={styles.cardDetail}>+254 700 000 000</Text>
                  <Text style={styles.cardMeta}>Mon–Fri · 8:00 AM – 5:00 PM</Text>
                </View>
                <Ionicons name="chevron-forward" size={18} color={colors.textMuted} />
              </TouchableOpacity>

              <TouchableOpacity style={styles.contactCard} onPress={() => onAction?.('prayer')}>
                <Ionicons name="hand-left-outline" size={24} color={colors.gold} />
                <View style={{ marginLeft: 16, flex: 1 }}>
                  <Text style={styles.cardTitle}>24/7 Prayer Line</Text>
                  <Text style={styles.cardDetail}>prayer@mavunochurch.org</Text>
                  <Text style={styles.cardMeta}>Confidential Pastoral Care</Text>
                </View>
                <Ionicons name="chevron-forward" size={18} color={colors.textMuted} />
              </TouchableOpacity>

              <TouchableOpacity style={styles.contactCard} onPress={() => onAction?.('map')}>
                <Ionicons name="location-outline" size={24} color={colors.orange} />
                <View style={{ marginLeft: 16, flex: 1 }}>
                  <Text style={styles.cardTitle}>Nairobi Campus Location</Text>
                  <Text style={styles.cardDetail}>Hill City Campus, Bellevue, South C</Text>
                  <Text style={styles.cardMeta}>Sunday Services: 9:00 AM & 11:30 AM</Text>
                </View>
                <Ionicons name="chevron-forward" size={18} color={colors.textMuted} />
              </TouchableOpacity>
            </ScrollView>
          )}

          {/* CHAT OVERLAY PANEL */}
          {type === 'chat' && (
            <View style={styles.chatBody}>
              <ScrollView style={{ flex: 1, paddingHorizontal: 16 }}>
                {messages.map((m) => (
                  <View
                    key={m.id}
                    style={[
                      styles.chatBubble,
                      m.sender === 'user' ? styles.userBubble : styles.botBubble,
                    ]}
                  >
                    <Text
                      style={[
                        styles.chatText,
                        m.sender === 'user' ? styles.userChatText : styles.botChatText,
                      ]}
                    >
                      {m.text}
                    </Text>
                    <Text style={styles.chatTime}>{m.time}</Text>
                  </View>
                ))}
              </ScrollView>

              <View style={styles.inputRow}>
                <TextInput
                  style={styles.chatInput}
                  placeholder="Ask a question or request prayer..."
                  placeholderTextColor={colors.textMuted}
                  value={chatInput}
                  onChangeText={setChatInput}
                />
                <TouchableOpacity style={styles.sendBtn} onPress={handleSendChat}>
                  <Ionicons name="send" size={18} color={colors.white} />
                </TouchableOpacity>
              </View>
            </View>
          )}

          {/* VIDEO / SERMON PLAYER OVERLAY PANEL */}
          {type === 'video' && (
            <ScrollView style={styles.body}>
              <View style={styles.videoPlayerPlaceholder}>
                <Ionicons name="play-circle" size={64} color={colors.orange} />
                <Text style={styles.videoPlayingTitle}>Rooted: Living from Purpose</Text>
                <Text style={styles.videoPlayingMeta}>Pastor James · Live Stream & Notes</Text>
              </View>
              <Text style={styles.sectionHeader}>Sermon Highlights & Scripture</Text>
              <Text style={styles.sermonText}>
                “Blessed are the peacemakers, for they will be called children of God.” — Matthew 5:9{'\n\n'}
                In this message, Pastor James explores how living with intentional purpose transforms our daily work, relationships, and faith journey.
              </Text>
            </ScrollView>
          )}

          {/* GIVING OVERLAY PANEL */}
          {type === 'give' && (
            <ScrollView style={styles.body}>
              <Text style={styles.subtitle}>Support the work of Mavuno Church via M-Pesa or Card</Text>

              <View style={styles.giveOptionCard}>
                <Ionicons name="phone-portrait-outline" size={24} color={colors.orange} />
                <View style={{ marginLeft: 16, flex: 1 }}>
                  <Text style={styles.cardTitle}>M-Pesa Paybill</Text>
                  <Text style={styles.cardDetail}>Business No: 508000</Text>
                  <Text style={styles.cardMeta}>Account: Tithe / Offering / Building</Text>
                </View>
              </View>

              <View style={styles.giveOptionCard}>
                <Ionicons name="card-outline" size={24} color={colors.gold} />
                <View style={{ marginLeft: 16, flex: 1 }}>
                  <Text style={styles.cardTitle}>Card / Online Payment</Text>
                  <Text style={styles.cardDetail}>Secure Visa & Mastercard</Text>
                  <Text style={styles.cardMeta}>Instant digital receipt</Text>
                </View>
              </View>
            </ScrollView>
          )}
        </TouchableOpacity>
      </TouchableOpacity>
    </Modal>
  );
}

const styles = StyleSheet.create({
  backdrop: {
    flex: 1,
    backgroundColor: 'rgba(15, 23, 42, 0.45)',
    justifyContent: 'flex-end',
  },
  modalContent: {
    backgroundColor: colors.surface,
    borderTopLeftRadius: radii.xl,
    borderTopRightRadius: radii.xl,
    maxHeight: '80%',
    minHeight: '45%',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: -10 },
    shadowOpacity: 0.15,
    shadowRadius: 20,
    elevation: 20,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 24,
    paddingVertical: 18,
    borderBottomWidth: 1,
    borderBottomColor: colors.line,
  },
  headerTitleRow: { flexDirection: 'row', alignItems: 'center' },
  iconCircle: {
    width: 36,
    height: 36,
    borderRadius: 18,
    backgroundColor: colors.orange,
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 12,
  },
  headerTitle: { fontSize: 16, fontWeight: '700', color: colors.textPrimary },
  closeBtn: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: colors.surfaceAlt,
    alignItems: 'center',
    justifyContent: 'center',
  },
  body: { padding: 24 },
  subtitle: { fontSize: 13, color: colors.textSecondary, marginBottom: 18 },
  contactCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.surfaceAlt,
    borderRadius: radii.md,
    padding: 16,
    marginBottom: 12,
  },
  giveOptionCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.surfaceAlt,
    borderRadius: radii.md,
    padding: 16,
    marginBottom: 12,
  },
  cardTitle: { fontSize: 14, fontWeight: '700', color: colors.textPrimary },
  cardDetail: { fontSize: 13, fontWeight: '600', color: colors.orange, marginTop: 2 },
  cardMeta: { fontSize: 11, color: colors.textMuted, marginTop: 2 },
  chatBody: { height: 350, paddingVertical: 16 },
  chatBubble: { maxWidth: '80%', borderRadius: 16, padding: 12, marginBottom: 10 },
  userBubble: { alignSelf: 'flex-end', backgroundColor: colors.orange },
  botBubble: { alignSelf: 'flex-start', backgroundColor: colors.surfaceAlt },
  chatText: { fontSize: 13 },
  userChatText: { color: colors.white },
  botChatText: { color: colors.textPrimary },
  chatTime: { fontSize: 10, color: colors.textMuted, marginTop: 4, textAlign: 'right' },
  inputRow: { flexDirection: 'row', paddingHorizontal: 16, paddingTop: 8, alignItems: 'center' },
  chatInput: {
    flex: 1,
    height: 44,
    backgroundColor: colors.surfaceAlt,
    borderRadius: 22,
    paddingHorizontal: 18,
    fontSize: 13,
    color: colors.textPrimary,
  },
  sendBtn: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: colors.orange,
    alignItems: 'center',
    justifyContent: 'center',
    marginLeft: 10,
  },
  videoPlayerPlaceholder: {
    height: 180,
    backgroundColor: colors.surfaceDark,
    borderRadius: radii.md,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 18,
  },
  videoPlayingTitle: { color: colors.white, fontSize: 16, fontWeight: '700', marginTop: 10 },
  videoPlayingMeta: { color: colors.textMuted, fontSize: 12, marginTop: 4 },
  sectionHeader: { fontSize: 14, fontWeight: '700', color: colors.textPrimary, marginBottom: 8 },
  sermonText: { fontSize: 13, color: colors.textSecondary, lineHeight: 20 },
});
